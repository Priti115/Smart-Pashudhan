import { useMemo, useState } from 'react'
import { ATSRow, ATSState, YearType } from './types'
import Filters from './ui/Filters'
import SummaryCards from './ui/SummaryCards'
import IndiaMap from './ui/IndiaMap'
import DataTable from './ui/DataTable'
import { INDIAN_STATES } from './data/stateList'

export default function App() {
  const [state, setState] = useState<string>('')
  const [district, setDistrict] = useState<string>('')
  const [yearType, setYearType] = useState<YearType>('Calendar')
  const [year, setYear] = useState<number>(2024)

  // Generate deterministic mock data for every state
  const DATA: ATSState[] = useMemo(() => {
    const hash = (str: string) =>
      str.split('').reduce((acc, ch) => (acc * 31 + ch.charCodeAt(0)) >>> 0, 7)
    return INDIAN_STATES.map((name) => {
      const h = hash(name)
      const mid = 55 + (h % 36) // 55..90
      const width = 4 + (Math.floor(h / 101) % 8) // 4..11
      const low = Math.max(0, Math.round(mid - width / 2))
      const high = Math.min(100, Math.round(mid + width / 2))
      const total = 50000 + (h % 1950000)
      const cattle = Math.round(total * (50 + (h % 30)) / 100)
      const buffalo = total - cattle
      const districts: ATSRow[] = [1, 2, 3].slice(0, 2 + (h % 2)).map((i) => ({
        state: name,
        district: `${name.split(' ')[0]} Dist ${i}`,
        totalEvaluated: Math.round(total / (2 + (h % 2)) * (0.8 + 0.4 * (i / 3))),
        cattle: Math.round(cattle / (2 + (h % 2)) * (0.8 + 0.4 * (i / 3))),
        buffalo: Math.round(buffalo / (2 + (h % 2)) * (0.8 + 0.4 * (i / 3))),
        avgScore: mid + (i - 2) * 2,
        avgRange: `${low}–${high}`,
      }))
      return {
        state: name,
        avgScore: mid,
        avgRange: `${low}–${high}`,
        totalEvaluated: total,
        cattle,
        buffalo,
        districts,
      }
    })
  }, [])

  const states = useMemo(() => DATA.map(s => s.state).sort(), [DATA])
  const districts = useMemo(() => {
    const sObj = DATA.find(ss => ss.state === state)
    return sObj ? sObj.districts.map(d => d.district!).sort() : []
  }, [state, DATA])

  const filteredRows: ATSRow[] = useMemo(() => {
    if (!state) {
      return DATA.map(s => ({
        state: s.state,
        totalEvaluated: s.totalEvaluated,
        cattle: s.cattle,
        buffalo: s.buffalo,
        avgRange: s.avgRange,
        avgScore: s.avgScore,
      }))
    }
    const sObj = DATA.find(ss => ss.state === state)
    return sObj ? sObj.districts : []
  }, [state, DATA])

  // Build range summaries for cards
  const rangeParts = DATA.map(s => s.avgRange?.split('–').map(n => parseFloat(n)) as [number, number])
  const nationalLow = Math.min(...rangeParts.map(r => r[0]))
  const nationalHigh = Math.max(...rangeParts.map(r => r[1]))
  const nationalText = `${nationalLow}–${nationalHigh}`

  const highest = useMemo(() => {
    const h = DATA.reduce((a, b) => (a.avgScore! > b.avgScore! ? a : b))
    return { state: h.state, text: h.avgRange }
  }, [DATA])
  const lowest = useMemo(() => {
    const l = DATA.reduce((a, b) => (a.avgScore! < b.avgScore! ? a : b))
    return { state: l.state, text: l.avgRange }
  }, [DATA])

  return (
    <div className="container-max py-6 space-y-6">
      <header className="flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-primary-800">ATS Score Dashboard</h1>
      </header>

      <section className="card p-4 md:p-6">
        <Filters
          states={states}
          districts={districts}
          value={{ state, district, yearType, year }}
          onChange={{ setState, setDistrict, setYearType, setYear }}
        />
      </section>

      <SummaryCards
        nationalText={nationalText}
        highest={highest}
        lowest={lowest}
      />

      <section className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Left column (Area Details) */}
        <div className="card p-4 md:p-6 lg:col-span-5 xl:col-span-4 overflow-hidden lg:order-1">
          <h2 className="text-lg font-semibold mb-4">Area Details{state ? ` - ${state}` : ''}</h2>
          <DataTable rows={filteredRows} showDistrict={!!state} />
        </div>
        {/* Right column (Map) */}
        <div className="card p-2 md:p-4 lg:col-span-7 xl:col-span-8 lg:order-2">
          <IndiaMap
            data={DATA.map(s => ({ id: s.state, value: s.avgScore, range: s.avgRange }))}
            selectedState={state}
            onSelectState={setState}
          />
        </div>
      </section>

      <footer className="text-center text-xs text-slate-500 pb-6">Data shown is sample for UI only</footer>
    </div>
  )
}

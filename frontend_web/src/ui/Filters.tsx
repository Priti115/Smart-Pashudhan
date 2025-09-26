import { YearType } from '../types'

type FiltersProps = {
  states: string[]
  districts: string[]
  value: {
    state: string
    district: string
    yearType: YearType
    year: number
  }
  onChange: {
    setState: (v: string) => void
    setDistrict: (v: string) => void
    setYearType: (v: YearType) => void
    setYear: (v: number) => void
  }
}

export default function Filters({ states, districts, value, onChange }: FiltersProps) {
  const years = Array.from({ length: 6 }, (_, i) => 2020 + i)

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div className="flex flex-col">
        <label className="text-base md:text-lg text-slate-700 mb-1 font-medium">State</label>
<select className="h-14 rounded-xl px-4 bg-primary-500 text-white text-xl border border-primary-600 hover:bg-primary-600 focus:outline-none focus:ring-2 focus:ring-primary-300" value={value.state} onChange={e => { onChange.setState(e.target.value); onChange.setDistrict('') }}>
          <option value="">All States</option>
          {states.map(s => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
      </div>

      <div className="flex flex-col">
        <label className="text-base md:text-lg text-slate-700 mb-1 font-medium">District</label>
<select className="h-14 rounded-xl px-4 bg-primary-500 text-white text-xl border border-primary-600 hover:bg-primary-600 focus:outline-none focus:ring-2 focus:ring-primary-300 disabled:opacity-60 disabled:cursor-not-allowed" value={value.district} onChange={e => onChange.setDistrict(e.target.value)} disabled={!value.state}>
          <option value="">All Districts</option>
          {districts.map(d => (
            <option key={d} value={d}>{d}</option>
          ))}
        </select>
      </div>


      <div className="flex flex-col">
        <label className="text-base md:text-lg text-slate-700 mb-1 font-medium">Year</label>
<select className="h-14 rounded-xl px-4 bg-primary-500 text-white text-xl border border-primary-600 hover:bg-primary-600 focus:outline-none focus:ring-2 focus:ring-primary-300" value={value.year} onChange={e => onChange.setYear(parseInt(e.target.value))}>
          {years.map(y => <option key={y} value={y}>{y}</option>)}
        </select>
      </div>

    </div>
  )
}

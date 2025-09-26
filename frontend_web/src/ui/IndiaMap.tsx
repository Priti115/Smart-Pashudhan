import { ComposableMap, Geographies, Geography } from 'react-simple-maps'
import { useMemo } from 'react'
import { Tooltip } from 'react-tooltip'

// You can replace this with a local file later if you have a preferred source.
// This URL should point to a GeoJSON/TopoJSON of India states (Admin-1 level).
const INDIA_GEO_URL =
  'https://cdn.jsdelivr.net/npm/india-geojson@1/india_states.geojson'

type IndiaMapProps = {
  data: { id: string; value?: number; range?: string }[] // id = state name
  selectedState: string
  onSelectState: (s: string) => void
}

export default function IndiaMap({ data, selectedState, onSelectState }: IndiaMapProps) {

  const valueById = useMemo(() => {
    const map = new Map<string, number | undefined>()
    data.forEach(d => map.set(d.id, d.value))
    return map
  }, [data])

  const rangeById = useMemo(() => {
    const map = new Map<string, string | undefined>()
    data.forEach(d => map.set(d.id, d.range))
    return map
  }, [data])

  const color = (v?: number) => {
    if (v == null) return '#E5F4EA' // very light
    if (v >= 85) return '#1f9254'
    if (v >= 75) return '#2bb673'
    if (v >= 65) return '#66cdaa'
    if (v >= 55) return '#a8e7cc'
    return '#dff6ea'
  }

  return (
    <div className="w-full h-[520px]">
      <ComposableMap projectionConfig={{ scale: 900 }} data-tip="" style={{ width: '100%', height: '100%' }}>
        <Geographies geography={INDIA_GEO_URL}>
          {({ geographies }: any) =>
            (geographies as any[]).map((geo: any) => {
              const name = (geo.properties as any).st_nm || (geo.properties as any).name || ''
              const v = valueById.get(name)
              const isSelected = selectedState && selectedState === name
              return (
                <Geography
                  key={geo.rsmKey}
                  geography={geo}
                  onClick={() => onSelectState(name === selectedState ? '' : name)}
                  data-tooltip-id="india-tip"
                  data-tooltip-content={`${name}: ${rangeById.get(name) ?? 'NA'}`}
                  style={{
                    default: { fill: color(v), outline: 'none', stroke: isSelected ? '#124C60' : '#cbd5e1', strokeWidth: isSelected ? 2 : 1 },
                    hover: { fill: v ? '#17637d' : '#d3e5ea', outline: 'none' },
                    pressed: { fill: v ? '#0f3f50' : '#c3dbe1', outline: 'none' },
                  }}
                />
              )
            })
          }
        </Geographies>
      </ComposableMap>
      <Tooltip id="india-tip" />
    </div>
  )
}

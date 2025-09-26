import { ATSRow } from '../types'

export default function DataTable({ rows, showDistrict }: { rows: ATSRow[]; showDistrict: boolean }) {
  const fmt = (n?: number) => (n == null ? '--' : n.toLocaleString())
  const fmtScore = (n?: number) => (n == null ? '--' : n.toFixed(1))
  return (
    <div className="overflow-auto">
      <table className="min-w-full text-sm">
        <thead>
          <tr className="bg-primary-50 text-slate-700">
            <th className="text-left font-medium px-4 py-3">{showDistrict ? 'District' : 'State'}</th>
            <th className="text-right font-medium px-4 py-3">Total Evaluated</th>
            <th className="text-right font-medium px-4 py-3">Cattle</th>
            <th className="text-right font-medium px-4 py-3">Buffalo</th>
            <th className="text-right font-medium px-4 py-3">Avg. ATS Range</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((r, idx) => (
            <tr key={(r.district || r.state) + idx} className={idx % 2 === 0 ? 'bg-white' : 'bg-slate-50/50'}>
              <td className="px-4 py-3 whitespace-nowrap">{showDistrict ? r.district : r.state}</td>
              <td className="px-4 py-3 text-right tabular-nums">{fmt(r.totalEvaluated)}</td>
              <td className="px-4 py-3 text-right tabular-nums">{fmt(r.cattle)}</td>
              <td className="px-4 py-3 text-right tabular-nums">{fmt(r.buffalo)}</td>
              <td className="px-4 py-3 text-right font-medium text-slate-800">{r.avgRange ?? fmtScore(r.avgScore)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

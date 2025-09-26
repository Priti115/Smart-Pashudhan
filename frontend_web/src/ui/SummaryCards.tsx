type SummaryCardsProps = {
  nationalText?: string
  highest?: { state: string; text?: string }
  lowest?: { state: string; text?: string }
}

export default function SummaryCards({ nationalText, highest, lowest }: SummaryCardsProps) {
  const card = 'card p-4 md:p-6'
  const metric = 'text-2xl font-semibold text-slate-800'
  const label = 'text-xs uppercase tracking-wide text-slate-500'

  return (
    <section className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div className={card}>
        <div className={label}>National Avg ATS Range</div>
        <div className={metric}>{nationalText ?? '--'}</div>
      </div>
      <div className={card}>
        <div className={label}>Highest State</div>
        <div className="flex items-baseline gap-2">
          <div className={metric}>{highest?.text ?? '--'}</div>
          <div className="text-sm text-slate-600">{highest?.state ?? '--'}</div>
        </div>
      </div>
      <div className={card}>
        <div className={label}>Lowest State</div>
        <div className="flex items-baseline gap-2">
          <div className={metric}>{lowest?.text ?? '--'}</div>
          <div className="text-sm text-slate-600">{lowest?.state ?? '--'}</div>
        </div>
      </div>
    </section>
  )
}

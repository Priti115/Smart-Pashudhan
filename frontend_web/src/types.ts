export type YearType = 'Calendar' | 'Financial'

export type ATSRow = {
  state: string
  district?: string
  totalEvaluated?: number
  cattle?: number
  buffalo?: number
  avgScore?: number // numeric midpoint for map coloring
  avgRange?: string // e.g., "68–74"
}

export type ATSState = {
  state: string
  avgScore?: number
  avgRange?: string
  totalEvaluated?: number
  cattle?: number
  buffalo?: number
  districts: ATSRow[]
}

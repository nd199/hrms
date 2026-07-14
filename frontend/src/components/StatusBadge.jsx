const styles = {
  ACTIVE: 'bg-emerald-50 text-emerald-700 ring-emerald-600/20 shadow-sm shadow-emerald-500/10',
  ON_LEAVE: 'bg-amber-50 text-amber-700 ring-amber-600/20 shadow-sm shadow-amber-500/10',
  PROBATION: 'bg-orange-50 text-orange-700 ring-orange-600/20 shadow-sm shadow-orange-500/10',
  RESIGNED: 'bg-gray-100 text-gray-600 ring-gray-500/20',
  TERMINATED: 'bg-red-50 text-red-700 ring-red-600/20 shadow-sm shadow-red-500/10',
}

const dotColors = {
  ACTIVE: 'bg-emerald-500',
  ON_LEAVE: 'bg-amber-500',
  PROBATION: 'bg-orange-500',
  TERMINATED: 'bg-red-500',
}

export default function StatusBadge({ status }) {
  return (
    <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg text-[11px] font-bold uppercase tracking-wider ring-1 ring-inset ${styles[status] || 'bg-gray-100 text-gray-600 ring-gray-500/20'}`}>
      <span className={`w-1.5 h-1.5 rounded-full ${dotColors[status] || 'bg-gray-400'}`} />
      {status?.replace('_', ' ')}
    </span>
  )
}

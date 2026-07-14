export function Skeleton({ className = '' }) {
  return <div className={`animate-pulse bg-gradient-to-r from-gray-200 via-gray-100 to-gray-200 rounded-lg ${className}`} />
}

export function TableRowSkeleton() {
  return (
    <tr className="border-b border-gray-50">
      <td className="px-6 py-4">
        <div className="flex items-center gap-3">
          <Skeleton className="w-9 h-9 rounded-full" />
          <div className="space-y-2">
            <Skeleton className="h-3.5 w-28" />
            <Skeleton className="h-3 w-36" />
          </div>
        </div>
      </td>
      <td className="px-6 py-4"><Skeleton className="h-3.5 w-16" /></td>
      <td className="px-6 py-4"><Skeleton className="h-3.5 w-20" /></td>
      <td className="px-6 py-4"><Skeleton className="h-3.5 w-24" /></td>
      <td className="px-6 py-4"><Skeleton className="h-6 w-16 rounded-lg" /></td>
      <td className="px-6 py-4"><Skeleton className="h-3.5 w-20" /></td>
      <td className="px-6 py-4"><Skeleton className="h-3.5 w-16 ml-auto" /></td>
    </tr>
  )
}

export function StatCardSkeleton() {
  return (
    <div className="bg-white/80 rounded-2xl p-5 shadow-sm border border-gray-200/60">
      <div className="flex items-center justify-between mb-3">
        <Skeleton className="h-3.5 w-20" />
        <Skeleton className="w-10 h-10 rounded-xl" />
      </div>
      <Skeleton className="h-10 w-14" />
    </div>
  )
}

export function DetailSkeleton() {
  return (
    <div className="space-y-6">
      <div className="flex items-center gap-4">
        <Skeleton className="w-16 h-16 rounded-2xl" />
        <div className="space-y-2">
          <Skeleton className="h-5 w-40" />
          <Skeleton className="h-3.5 w-56" />
        </div>
      </div>
      <div className="grid grid-cols-2 gap-5">
        {Array.from({ length: 6 }).map((_, i) => (
          <div key={i} className="space-y-2">
            <Skeleton className="h-3 w-16" />
            <Skeleton className="h-4 w-32" />
          </div>
        ))}
      </div>
    </div>
  )
}

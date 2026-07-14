import { useState, useEffect } from 'react'
import { employeeApi } from '../api/employee'
import { Link } from 'react-router-dom'
import StatusBadge from '../components/StatusBadge'
import { StatCardSkeleton } from '../components/Skeleton'

const stats = [
  {
    label: 'Total Employees',
    key: 'total',
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
      </svg>
    ),
    gradient: 'from-indigo-500 to-violet-500',
    bg: 'stat-gradient-1',
    iconBg: 'bg-indigo-100 text-indigo-600',
  },
  {
    label: 'Active',
    key: 'ACTIVE',
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    ),
    gradient: 'from-emerald-500 to-teal-500',
    bg: 'stat-gradient-2',
    iconBg: 'bg-emerald-100 text-emerald-600',
  },
  {
    label: 'On Leave',
    key: 'ON_LEAVE',
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5" />
      </svg>
    ),
    gradient: 'from-amber-500 to-orange-500',
    bg: 'stat-gradient-3',
    iconBg: 'bg-amber-100 text-amber-600',
  },
  {
    label: 'Probation',
    key: 'PROBATION',
    icon: (
      <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    ),
    gradient: 'from-orange-500 to-rose-500',
    bg: 'stat-gradient-4',
    iconBg: 'bg-orange-100 text-orange-600',
  },
]

export default function Dashboard() {
  const [counts, setCounts] = useState({})
  const [recentEmployees, setRecentEmployees] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function load() {
      try {
        const allRes = await employeeApi.getAll({ page: 0, size: 1 })
        setCounts({ total: allRes.data.totalElements || 0 })

        const statuses = ['ACTIVE', 'ON_LEAVE', 'PROBATION']
        const results = await Promise.all(statuses.map(s => employeeApi.getByStatus(s)))
        statuses.forEach((s, i) => { setCounts(prev => ({ ...prev, [s]: results[i].data.length })) })

        const recentRes = await employeeApi.getAll({ page: 0, size: 5, sort: 'createdAt,desc' })
        setRecentEmployees(recentRes.data.content || [])
      } catch (err) {
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-sm text-gray-500 mt-1">Overview of your workforce</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
        {loading ? (
          Array.from({ length: 4 }).map((_, i) => <StatCardSkeleton key={i} />)
        ) : stats.map(({ label, key, icon, bg, iconBg, gradient }) => (
          <div key={key} className={`relative overflow-hidden rounded-2xl ${bg} p-5 border border-white/60 shadow-sm hover:shadow-lg transition-all duration-300 hover:-translate-y-0.5`}>
            <div className={`absolute top-0 right-0 w-24 h-24 bg-gradient-to-br ${gradient} opacity-[0.07] rounded-bl-[3rem]`} />
            <div className="relative">
              <div className="flex items-center justify-between mb-3">
                <p className="text-sm font-medium text-gray-600">{label}</p>
                <div className={`w-10 h-10 ${iconBg} rounded-xl flex items-center justify-center`}>
                  {icon}
                </div>
              </div>
              <p className="text-4xl font-extrabold text-gray-900 tracking-tight">
                {counts[key] ?? 0}
              </p>
            </div>
          </div>
        ))}
      </div>

      <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-sm border border-gray-200/60 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <div>
            <h2 className="text-lg font-bold text-gray-900">Recent Employees</h2>
            <p className="text-xs text-gray-400 mt-0.5">Latest additions to your team</p>
          </div>
          <Link
            to="/employees"
            className="inline-flex items-center gap-1 text-sm font-semibold text-indigo-600 hover:text-indigo-700"
          >
            View all
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
            </svg>
          </Link>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-100">
                <th className="text-left text-[11px] font-semibold text-gray-400 uppercase tracking-wider px-6 py-3">Employee</th>
                <th className="text-left text-[11px] font-semibold text-gray-400 uppercase tracking-wider px-6 py-3">Code</th>
                <th className="text-left text-[11px] font-semibold text-gray-400 uppercase tracking-wider px-6 py-3">Department</th>
                <th className="text-left text-[11px] font-semibold text-gray-400 uppercase tracking-wider px-6 py-3">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {recentEmployees.length === 0 ? (
                <tr>
                  <td colSpan={4} className="px-6 py-16 text-center">
                    <div className="flex flex-col items-center gap-3">
                      <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-indigo-100 to-violet-100 flex items-center justify-center">
                        <svg className="w-8 h-8 text-indigo-400" fill="none" viewBox="0 0 24 24" strokeWidth="1" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
                        </svg>
                      </div>
                      <p className="text-sm font-medium text-gray-900">No employees yet</p>
                      <Link
                        to="/employees/new"
                        className="inline-flex items-center gap-1.5 px-4 py-2 bg-gradient-to-r from-indigo-500 to-violet-500 text-white text-sm font-semibold rounded-xl hover:from-indigo-600 hover:to-violet-600 transition-all shadow-md shadow-indigo-500/25"
                      >
                        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
                        </svg>
                        Add your first employee
                      </Link>
                    </div>
                  </td>
                </tr>
              ) : recentEmployees.map((emp, idx) => (
                <tr key={emp.id} className={`hover:bg-indigo-50/30 transition-colors duration-150 ${idx % 2 === 1 ? 'bg-gray-50/40' : ''}`}>
                  <td className="px-6 py-3.5">
                    <div className="flex items-center gap-3">
                      <div className="w-9 h-9 rounded-full bg-gradient-to-br from-indigo-400 to-violet-500 flex items-center justify-center text-white text-xs font-bold shadow-md shadow-indigo-300/30 shrink-0">
                        {emp.firstName?.[0]}{emp.lastName?.[0]}
                      </div>
                      <div className="min-w-0">
                        <p className="text-sm font-semibold text-gray-900 truncate">{emp.firstName} {emp.lastName}</p>
                        <p className="text-xs text-gray-400 truncate">{emp.email}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-3.5 text-sm text-gray-600 font-mono">{emp.employeeCode}</td>
                  <td className="px-6 py-3.5 text-sm text-gray-600">{emp.departmentName || '-'}</td>
                  <td className="px-6 py-3.5">
                    <StatusBadge status={emp.employmentStatus} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

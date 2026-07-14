import { useState, useEffect } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { employeeApi } from '../api/employee'
import StatusBadge from '../components/StatusBadge'
import { DetailSkeleton } from '../components/Skeleton'

export default function EmployeeDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [employee, setEmployee] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    employeeApi.getById(id)
      .then(res => setEmployee(res.data))
      .catch(err => setError(err.response?.data?.message || 'Employee not found'))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) {
    return (
      <div className="p-8 max-w-4xl">
        <div className="flex items-center gap-3 mb-6">
          <div className="w-8 h-8 rounded-xl bg-gray-200 animate-pulse" />
          <div className="h-6 w-40 bg-gray-200 rounded-lg animate-pulse" />
        </div>
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200/60 p-6">
          <DetailSkeleton />
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="p-8 max-w-4xl">
        <div className="flex items-center gap-3 mb-6">
          <button onClick={() => navigate(-1)} className="p-2 text-gray-400 hover:text-gray-600 rounded-xl hover:bg-gray-100 transition-all">
            <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
            </svg>
          </button>
          <h1 className="text-2xl font-bold text-gray-900">Employee Details</h1>
        </div>
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200/60 p-12 text-center">
          <div className="w-16 h-16 rounded-2xl bg-red-50 flex items-center justify-center mx-auto mb-3">
            <svg className="w-8 h-8 text-red-400" fill="none" viewBox="0 0 24 24" strokeWidth="1" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
            </svg>
          </div>
          <p className="text-sm font-semibold text-gray-900">{error}</p>
          <button onClick={() => navigate('/employees')} className="mt-4 text-sm font-semibold text-indigo-600 hover:text-indigo-700">
            Back to employees
          </button>
        </div>
      </div>
    )
  }

  if (!employee) return null

  return (
    <div className="p-8 max-w-4xl">
      <div className="flex items-center gap-3 mb-6">
        <button onClick={() => navigate(-1)} className="p-2 text-gray-400 hover:text-gray-600 rounded-xl hover:bg-gray-100 transition-all">
          <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
        </button>
        <h1 className="text-2xl font-bold text-gray-900">Employee Details</h1>
      </div>

      <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-sm border border-gray-200/60 overflow-hidden">
        <div className="relative p-6 border-b border-gray-100 bg-gradient-to-r from-indigo-50/80 via-violet-50/50 to-purple-50/80">
          <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top_right,rgba(139,92,246,0.08),transparent_50%)]" />
          <div className="relative flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-indigo-400 to-violet-500 flex items-center justify-center text-white font-bold text-xl shadow-lg shadow-indigo-400/30 shrink-0">
                {employee.firstName?.[0]}{employee.lastName?.[0]}
              </div>
              <div className="min-w-0">
                <h2 className="text-xl font-bold text-gray-900">{employee.firstName} {employee.lastName}</h2>
                <p className="text-sm text-gray-500 mt-0.5">{employee.employeeCode} &middot; {employee.email}</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <StatusBadge status={employee.employmentStatus} />
              <Link
                to={`/employees/${id}/edit`}
                className="inline-flex items-center gap-1.5 px-4 py-2.5 bg-gradient-to-r from-indigo-500 to-violet-500 text-white rounded-xl text-sm font-semibold hover:from-indigo-600 hover:to-violet-600 transition-all shadow-md shadow-indigo-500/25"
              >
                <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125" />
                </svg>
                Edit
              </Link>
            </div>
          </div>
        </div>

        <div className="p-6">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Personal Information</h3>
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-5">
            <Field label="Email" value={employee.email} />
            <Field label="Phone" value={employee.phone} />
            <Field label="Gender" value={employee.gender?.replace('_', ' ')} />
            <Field label="Date of Birth" value={employee.dateOfBirth ? new Date(employee.dateOfBirth).toLocaleDateString() : null} />
            <Field label="Hire Date" value={employee.hireDate ? new Date(employee.hireDate).toLocaleDateString() : null} />
            <Field label="Department" value={employee.departmentName} />
            <Field label="Position" value={employee.positionTitle} />
            <Field label="Manager" value={employee.managerName} />
            <Field label="Shift" value={employee.shiftName} />
          </div>
        </div>

        <div className="p-6 border-t border-gray-100">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Address</h3>
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-5">
            <Field label="Address" value={employee.address} />
            <Field label="City" value={employee.city} />
            <Field label="State" value={employee.state} />
            <Field label="Country" value={employee.country} />
            <Field label="Postal Code" value={employee.postalCode} />
          </div>
        </div>

        <div className="p-6 border-t border-gray-100">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Emergency Contact</h3>
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-5">
            <Field label="Name" value={employee.emergencyContactName} />
            <Field label="Phone" value={employee.emergencyContactPhone} />
          </div>
        </div>

        <div className="px-6 py-3 border-t border-gray-100 bg-gradient-to-r from-gray-50/80 to-slate-50/80 text-[11px] text-gray-400 flex gap-6">
          <span>Created: {employee.createdAt ? new Date(employee.createdAt).toLocaleString() : '-'}</span>
          <span>Updated: {employee.updatedAt ? new Date(employee.updatedAt).toLocaleString() : '-'}</span>
        </div>
      </div>
    </div>
  )
}

function Field({ label, value }) {
  return (
    <div>
      <p className="text-[11px] text-gray-400 font-medium uppercase tracking-wider mb-1">{label}</p>
      <p className="text-sm text-gray-900 font-medium">{value || '-'}</p>
    </div>
  )
}

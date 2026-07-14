import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { employeeApi } from '../api/employee'
import { useToast } from '../components/Toast'

const genderOptions = ['MALE', 'FEMALE', 'OTHER']
const statusOptions = ['ACTIVE', 'ON_LEAVE', 'PROBATION', 'RESIGNED', 'TERMINATED']

const initialForm = {
  employeeCode: '', firstName: '', lastName: '', email: '', phone: '',
  gender: '', dateOfBirth: '', hireDate: '', employmentStatus: 'ACTIVE',
  address: '', city: '', state: '', country: '', postalCode: '',
  emergencyContactName: '', emergencyContactPhone: '',
  departmentId: '', positionId: '', managerId: '', shiftId: '',
}

export default function EmployeeForm() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isEdit = Boolean(id)
  const toast = useToast()
  const [form, setForm] = useState(initialForm)
  const [loading, setLoading] = useState(false)
  const [fetching, setFetching] = useState(isEdit)
  const [errors, setErrors] = useState({})
  const [serverError, setServerError] = useState(null)

  useEffect(() => {
    if (isEdit) {
      employeeApi.getById(id)
        .then(res => {
          const emp = res.data
          setForm({
            employeeCode: emp.employeeCode || '',
            firstName: emp.firstName || '',
            lastName: emp.lastName || '',
            email: emp.email || '',
            phone: emp.phone || '',
            gender: emp.gender || '',
            dateOfBirth: emp.dateOfBirth ? emp.dateOfBirth.split('T')[0] : '',
            hireDate: emp.hireDate ? emp.hireDate.split('T')[0] : '',
            employmentStatus: emp.employmentStatus || 'ACTIVE',
            address: emp.address || '',
            city: emp.city || '',
            state: emp.state || '',
            country: emp.country || '',
            postalCode: emp.postalCode || '',
            emergencyContactName: emp.emergencyContactName || '',
            emergencyContactPhone: emp.emergencyContactPhone || '',
            departmentId: emp.departmentId || '',
            positionId: emp.positionId || '',
            managerId: emp.managerId || '',
            shiftId: emp.shiftId || '',
          })
        })
        .catch(() => setServerError('Failed to load employee'))
        .finally(() => setFetching(false))
    }
  }, [id, isEdit])

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: null }))
  }

  const validate = () => {
    const errs = {}
    if (!form.employeeCode.trim()) errs.employeeCode = 'Required'
    if (!form.firstName.trim()) errs.firstName = 'Required'
    if (!form.lastName.trim()) errs.lastName = 'Required'
    if (!form.email.trim()) errs.email = 'Required'
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) errs.email = 'Invalid email'
    if (!form.hireDate) errs.hireDate = 'Required'
    if (!form.departmentId) errs.departmentId = 'Required'
    setErrors(errs)
    return Object.keys(errs).length === 0
  }

  const toIso = (dateStr) => dateStr ? dateStr + 'T00:00:00' : null

  const handleSubmit = async (e) => {
    e.preventDefault()
    setServerError(null)
    if (!validate()) return

    const payload = {
      employeeCode: form.employeeCode.trim(),
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      email: form.email.trim(),
      phone: form.phone.trim() || null,
      gender: form.gender || null,
      dateOfBirth: toIso(form.dateOfBirth),
      hireDate: toIso(form.hireDate),
      employmentStatus: form.employmentStatus,
      address: form.address.trim() || null,
      city: form.city.trim() || null,
      state: form.state.trim() || null,
      country: form.country.trim() || null,
      postalCode: form.postalCode.trim() || null,
      emergencyContactName: form.emergencyContactName.trim() || null,
      emergencyContactPhone: form.emergencyContactPhone.trim() || null,
      departmentId: form.departmentId ? Number(form.departmentId) : null,
      positionId: form.positionId ? Number(form.positionId) : null,
      managerId: form.managerId ? Number(form.managerId) : null,
      shiftId: form.shiftId ? Number(form.shiftId) : null,
    }

    setLoading(true)
    try {
      if (isEdit) {
        await employeeApi.update(id, payload)
        toast('Employee updated successfully', 'success')
      } else {
        await employeeApi.create(payload)
        toast('Employee created successfully', 'success')
      }
      navigate('/employees')
    } catch (err) {
      setServerError(err.response?.data?.message || 'Something went wrong')
    } finally {
      setLoading(false)
    }
  }

  if (fetching) {
    return (
      <div className="p-8 max-w-3xl">
        <div className="flex items-center gap-3 mb-6">
          <div className="w-8 h-8 rounded-xl bg-gray-200 animate-pulse" />
          <div className="h-6 w-36 bg-gray-200 rounded-lg animate-pulse" />
        </div>
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200/60 p-6 space-y-6">
          {Array.from({ length: 3 }).map((_, i) => (
            <div key={i} className="space-y-4">
              <div className="h-4 w-28 bg-gray-200 rounded-lg animate-pulse" />
              <div className="grid grid-cols-2 gap-4">
                {Array.from({ length: 4 }).map((_, j) => (
                  <div key={j} className="space-y-2">
                    <div className="h-3 w-16 bg-gray-100 rounded animate-pulse" />
                    <div className="h-10 w-full bg-gray-100 rounded-xl animate-pulse" />
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div className="p-8 max-w-3xl">
      <div className="flex items-center gap-3 mb-6">
        <button onClick={() => navigate(-1)} className="p-2 text-gray-400 hover:text-gray-600 rounded-xl hover:bg-gray-100 transition-all">
          <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
        </button>
        <h1 className="text-2xl font-bold text-gray-900">{isEdit ? 'Edit Employee' : 'New Employee'}</h1>
      </div>

      {serverError && (
        <div className="mb-4 p-4 bg-red-50 border border-red-200 rounded-xl flex items-center gap-3">
          <svg className="w-5 h-5 text-red-500 shrink-0" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
          </svg>
          <p className="text-sm text-red-700">{serverError}</p>
        </div>
      )}

      <form onSubmit={handleSubmit} className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-sm border border-gray-200/60 overflow-hidden">
        <div className="p-6 border-b border-gray-100">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Basic Information</h3>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Employee Code" name="employeeCode" value={form.employeeCode} onChange={handleChange} error={errors.employeeCode} required />
            <Input label="Email" name="email" type="email" value={form.email} onChange={handleChange} error={errors.email} required />
            <Input label="First Name" name="firstName" value={form.firstName} onChange={handleChange} error={errors.firstName} required />
            <Input label="Last Name" name="lastName" value={form.lastName} onChange={handleChange} error={errors.lastName} required />
            <Input label="Phone" name="phone" value={form.phone} onChange={handleChange} />
            <Select label="Gender" name="gender" value={form.gender} onChange={handleChange} options={genderOptions} placeholder="Select..." />
            <Input label="Date of Birth" name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={handleChange} />
            <Input label="Hire Date" name="hireDate" type="date" value={form.hireDate} onChange={handleChange} error={errors.hireDate} required />
            <Select label="Status" name="employmentStatus" value={form.employmentStatus} onChange={handleChange} options={statusOptions} />
          </div>
        </div>

        <div className="p-6 border-b border-gray-100">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Organization</h3>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Department ID" name="departmentId" type="number" value={form.departmentId} onChange={handleChange} error={errors.departmentId} required />
            <Input label="Position ID" name="positionId" type="number" value={form.positionId} onChange={handleChange} />
            <Input label="Manager ID" name="managerId" type="number" value={form.managerId} onChange={handleChange} />
            <Input label="Shift ID" name="shiftId" type="number" value={form.shiftId} onChange={handleChange} />
          </div>
        </div>

        <div className="p-6 border-b border-gray-100">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Address</h3>
          <div className="grid grid-cols-2 gap-4">
            <div className="col-span-2">
              <Input label="Address" name="address" value={form.address} onChange={handleChange} />
            </div>
            <Input label="City" name="city" value={form.city} onChange={handleChange} />
            <Input label="State" name="state" value={form.state} onChange={handleChange} />
            <Input label="Country" name="country" value={form.country} onChange={handleChange} />
            <Input label="Postal Code" name="postalCode" value={form.postalCode} onChange={handleChange} />
          </div>
        </div>

        <div className="p-6">
          <h3 className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mb-4">Emergency Contact</h3>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Contact Name" name="emergencyContactName" value={form.emergencyContactName} onChange={handleChange} />
            <Input label="Contact Phone" name="emergencyContactPhone" value={form.emergencyContactPhone} onChange={handleChange} />
          </div>
        </div>

        <div className="px-6 py-4 border-t border-gray-100 bg-gradient-to-r from-gray-50/80 to-slate-50/80 flex justify-end gap-3">
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="px-5 py-2.5 border border-gray-200 rounded-xl text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={loading}
            className="px-5 py-2.5 bg-gradient-to-r from-indigo-500 to-violet-500 text-white rounded-xl text-sm font-semibold hover:from-indigo-600 hover:to-violet-600 disabled:opacity-50 transition-all shadow-md shadow-indigo-500/25"
          >
            {loading ? 'Saving...' : isEdit ? 'Update Employee' : 'Create Employee'}
          </button>
        </div>
      </form>
    </div>
  )
}

function Input({ label, name, type = 'text', value, onChange, error, required }) {
  return (
    <div>
      <label className="block text-[11px] text-gray-400 font-medium uppercase tracking-wider mb-1.5">
        {label}{required && <span className="text-red-500 ml-0.5">*</span>}
      </label>
      <input
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        className={`w-full px-3.5 py-2.5 border rounded-xl text-sm transition-all focus:ring-2 focus:ring-indigo-500/30 focus:border-indigo-400 outline-none ${
          error ? 'border-red-300 focus:ring-red-500/30 focus:border-red-400 bg-red-50/30' : 'border-gray-200 bg-white/60'
        }`}
      />
      {error && <p className="text-[11px] text-red-500 mt-1 font-medium">{error}</p>}
    </div>
  )
}

function Select({ label, name, value, onChange, options, placeholder }) {
  return (
    <div>
      <label className="block text-[11px] text-gray-400 font-medium uppercase tracking-wider mb-1.5">{label}</label>
      <select
        name={name}
        value={value}
        onChange={onChange}
        className="w-full px-3.5 py-2.5 border border-gray-200 rounded-xl text-sm bg-white/60 transition-all focus:ring-2 focus:ring-indigo-500/30 focus:border-indigo-400 outline-none"
      >
        {placeholder && <option value="">{placeholder}</option>}
        {options.map(opt => (
          <option key={opt} value={opt}>{opt.replace('_', ' ')}</option>
        ))}
      </select>
    </div>
  )
}

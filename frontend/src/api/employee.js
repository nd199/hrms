import { employees as seedEmployees } from '../lib/data'

let employees = structuredClone(seedEmployees)
let nextId = employees.length + 1

function delay(ms = 100) {
  return new Promise(r => setTimeout(r, ms))
}

function wrap(data) {
  return Promise.resolve({ data })
}

function paginate(list, { page = 0, size = 10, sort = 'id,desc' } = {}) {
  const [field, dir] = sort.split(',')
  const sorted = [...list].sort((a, b) => {
    const va = a[field] ?? ''
    const vb = b[field] ?? ''
    const cmp = typeof va === 'string' ? va.localeCompare(vb) : va - vb
    return dir === 'asc' ? cmp : -cmp
  })
  const start = page * size
  return {
    content: sorted.slice(start, start + size),
    totalElements: sorted.length,
    totalPages: Math.ceil(sorted.length / size),
    number: page,
    size,
  }
}

export const employeeApi = {
  async getAll(params = {}) {
    await delay()
    return wrap(paginate(employees, params))
  },

  async getById(id) {
    await delay()
    const emp = employees.find(e => e.id === Number(id))
    if (!emp) throw { response: { data: { message: 'Employee not found' } } }
    return wrap(emp)
  },

  async getByCode(code) {
    await delay()
    const emp = employees.find(e => e.employeeCode === code)
    if (!emp) throw { response: { data: { message: 'Employee not found' } } }
    return wrap(emp)
  },

  async getByDepartment(departmentId) {
    await delay()
    return wrap(employees.filter(e => e.departmentId === Number(departmentId)))
  },

  async getByStatus(status) {
    await delay()
    return wrap(employees.filter(e => e.employmentStatus === status))
  },

  async getByManager(managerId) {
    await delay()
    return wrap(employees.filter(e => e.managerId === Number(managerId)))
  },

  async search(keyword, params = {}) {
    await delay()
    const q = keyword.toLowerCase()
    const filtered = employees.filter(e =>
      e.firstName.toLowerCase().includes(q) ||
      e.lastName.toLowerCase().includes(q) ||
      e.email.toLowerCase().includes(q) ||
      e.employeeCode.toLowerCase().includes(q)
    )
    return wrap(paginate(filtered, params))
  },

  async create(data) {
    await delay()
    const emp = { ...data, id: nextId++, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() }
    employees = [emp, ...employees]
    return wrap(emp)
  },

  async update(id, data) {
    await delay()
    const idx = employees.findIndex(e => e.id === Number(id))
    if (idx === -1) throw { response: { data: { message: 'Employee not found' } } }
    employees[idx] = { ...employees[idx], ...data, updatedAt: new Date().toISOString() }
    return wrap(employees[idx])
  },

  async delete(id) {
    await delay()
    employees = employees.filter(e => e.id !== Number(id))
    return wrap({ success: true })
  },
}

export default employeeApi

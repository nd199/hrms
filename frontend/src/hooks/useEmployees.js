import { useState, useEffect, useCallback } from 'react'
import { employeeApi } from '../api/employee'

export function useEmployees() {
  const [employees, setEmployees] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [keyword, setKeyword] = useState('')

  const fetchEmployees = useCallback(async (currentPage = page, search = keyword) => {
    setLoading(true)
    setError(null)
    try {
      let response
      if (search.trim()) {
        response = await employeeApi.search(search, { page: currentPage, size: 10 })
      } else {
        response = await employeeApi.getAll({ page: currentPage, size: 10 })
      }
      const data = response.data
      setEmployees(data.content || [])
      setTotalPages(data.totalPages || 0)
      setTotalElements(data.totalElements || 0)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch employees')
      setEmployees([])
    } finally {
      setLoading(false)
    }
  }, [page, keyword])

  useEffect(() => {
    fetchEmployees()
  }, [])

  const searchEmployees = (term) => {
    setKeyword(term)
    setPage(0)
    fetchEmployees(0, term)
  }

  const goToPage = (newPage) => {
    setPage(newPage)
    fetchEmployees(newPage, keyword)
  }

  const deleteEmployee = async (id) => {
    await employeeApi.delete(id)
    fetchEmployees()
  }

  return {
    employees, loading, error, page, totalPages, totalElements,
    searchEmployees, goToPage, deleteEmployee, refetch: fetchEmployees,
  }
}

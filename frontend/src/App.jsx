import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import EmployeeList from './pages/EmployeeList'
import EmployeeDetail from './pages/EmployeeDetail'
import EmployeeForm from './pages/EmployeeForm'
import Placeholder from './pages/Placeholder'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/employees" element={<EmployeeList />} />
          <Route path="/employees/new" element={<EmployeeForm />} />
          <Route path="/employees/:id" element={<EmployeeDetail />} />
          <Route path="/employees/:id/edit" element={<EmployeeForm />} />
          <Route path="/departments" element={<Placeholder title="Departments" />} />
          <Route path="/attendance" element={<Placeholder title="Attendance" />} />
          <Route path="/leave" element={<Placeholder title="Leave Management" />} />
          <Route path="/payroll" element={<Placeholder title="Payroll" />} />
          <Route path="/performance" element={<Placeholder title="Performance Reviews" />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App

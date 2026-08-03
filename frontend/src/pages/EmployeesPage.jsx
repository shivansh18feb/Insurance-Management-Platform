import React, { useEffect, useState } from 'react';
import { adminApi } from '../services/api';
import { Briefcase, Plus, Building2, Calendar, Phone, X, CheckCircle } from 'lucide-react';

const DEPARTMENTS = ['Claims', 'Underwriting', 'Finance', 'HR', 'IT', 'Operations', 'Legal', 'Marketing'];

export const EmployeesPage = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', password: '',
    department: 'Claims', position: '', phoneNumber: '', hireDate: ''
  });

  useEffect(() => { fetchEmployees(); }, []);

  const fetchEmployees = async () => {
    try {
      const res = await adminApi.getEmployees();
      setEmployees(res.data.data || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await adminApi.createEmployee(form);
      setShowModal(false);
      setForm({ firstName: '', lastName: '', email: '', password: '', department: 'Claims', position: '', phoneNumber: '', hireDate: '' });
      fetchEmployees();
    } catch (err) {
      setError(err.response?.data?.message || 'Error creating employee');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Employee Management</h1>
          <p className="text-sm text-slate-400">Manage internal staff and department assignments</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Add Employee
        </button>
      </div>

      {/* Department Summary */}
      <div className="grid grid-cols-4 gap-4">
        {['Claims', 'Underwriting', 'Finance', 'IT'].map(dept => {
          const count = employees.filter(e => e.department === dept).length;
          return (
            <div key={dept} className="glass-card p-4 flex items-center gap-3">
              <div className="p-2.5 bg-indigo-500/20 rounded-xl">
                <Building2 className="w-5 h-5 text-indigo-400" />
              </div>
              <div>
                <p className="text-xs text-slate-400">{dept}</p>
                <p className="text-xl font-bold text-slate-100">{count}</p>
              </div>
            </div>
          );
        })}
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading employees...</div>
      ) : employees.length === 0 ? (
        <div className="glass-card p-12 text-center">
          <Briefcase className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <p className="text-slate-400">No employees found. Add your first employee!</p>
        </div>
      ) : (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Employee</th>
                <th className="px-6 py-4">Code</th>
                <th className="px-6 py-4">Department & Position</th>
                <th className="px-6 py-4">Contact</th>
                <th className="px-6 py-4">Hire Date</th>
                <th className="px-6 py-4">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {employees.map((emp) => (
                <tr key={emp.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-emerald-500 to-teal-600 flex items-center justify-center font-bold text-white text-sm">
                        {emp.firstName?.charAt(0)}{emp.lastName?.charAt(0)}
                      </div>
                      <div>
                        <p className="font-semibold text-slate-100">{emp.firstName} {emp.lastName}</p>
                        <p className="text-xs text-slate-400">{emp.email}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className="font-mono text-xs text-emerald-400 font-semibold">{emp.employeeCode}</span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1.5 mb-1">
                      <Building2 className="w-3.5 h-3.5 text-indigo-400" />
                      <span className="text-xs font-semibold text-indigo-300">{emp.department}</span>
                    </div>
                    <p className="text-xs text-slate-400">{emp.position}</p>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1.5 text-xs text-slate-400">
                      <Phone className="w-3.5 h-3.5 text-amber-400" />
                      {emp.phoneNumber || '—'}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1.5 text-xs text-slate-400">
                      <Calendar className="w-3.5 h-3.5 text-purple-400" />
                      {emp.hireDate || '—'}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${emp.active ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30' : 'bg-rose-500/20 text-rose-400 border-rose-500/30'}`}>
                      {emp.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Create Employee Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="glass-card max-w-lg w-full p-6 space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-bold text-slate-100">Onboard New Employee</h3>
              <button onClick={() => setShowModal(false)} className="p-1.5 hover:bg-slate-800 rounded-lg text-slate-400">
                <X className="w-4 h-4" />
              </button>
            </div>
            {error && <div className="bg-rose-500/10 border border-rose-500/30 rounded-xl px-4 py-3 text-sm text-rose-400">{error}</div>}
            <form onSubmit={handleCreate} className="space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <input type="text" placeholder="First Name" required className="glass-input" value={form.firstName} onChange={e => setForm({ ...form, firstName: e.target.value })} />
                <input type="text" placeholder="Last Name" required className="glass-input" value={form.lastName} onChange={e => setForm({ ...form, lastName: e.target.value })} />
              </div>
              <input type="email" placeholder="Work Email" required className="glass-input w-full" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
              <input type="password" placeholder="Temporary Password" required className="glass-input w-full" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
              <div className="grid grid-cols-2 gap-3">
                <select className="glass-input" value={form.department} onChange={e => setForm({ ...form, department: e.target.value })}>
                  {DEPARTMENTS.map(d => <option key={d} value={d}>{d}</option>)}
                </select>
                <input type="text" placeholder="Job Position" required className="glass-input" value={form.position} onChange={e => setForm({ ...form, position: e.target.value })} />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <input type="text" placeholder="Phone Number" className="glass-input" value={form.phoneNumber} onChange={e => setForm({ ...form, phoneNumber: e.target.value })} />
                <input type="date" className="glass-input" value={form.hireDate} onChange={e => setForm({ ...form, hireDate: e.target.value })} />
              </div>
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
                <button type="submit" className="btn-primary flex items-center gap-2">
                  <CheckCircle className="w-4 h-4" />
                  Onboard Employee
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

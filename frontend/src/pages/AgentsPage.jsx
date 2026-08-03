import React, { useEffect, useState } from 'react';
import { adminApi } from '../services/api';
import { UserCheck, Plus, Award, MapPin, Percent, X, CheckCircle } from 'lucide-react';

const SPECIALIZATIONS = ['LIFE', 'HEALTH', 'VEHICLE', 'PROPERTY', 'TRAVEL', 'GENERAL'];

export const AgentsPage = () => {
  const [agents, setAgents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', password: '',
    licenseNumber: '', specialization: 'LIFE', assignedRegion: '', commissionRate: '5'
  });

  useEffect(() => { fetchAgents(); }, []);

  const fetchAgents = async () => {
    try {
      const res = await adminApi.getAgents();
      setAgents(res.data.data || []);
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
      await adminApi.createAgent({ ...form, commissionRate: parseFloat(form.commissionRate) });
      setShowModal(false);
      setForm({ firstName: '', lastName: '', email: '', password: '', licenseNumber: '', specialization: 'LIFE', assignedRegion: '', commissionRate: '5' });
      fetchAgents();
    } catch (err) {
      setError(err.response?.data?.message || 'Error creating agent');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Agent Management</h1>
          <p className="text-sm text-slate-400">Manage insurance agents and their commissions</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Add Agent
        </button>
      </div>

      {/* Stats Bar */}
      <div className="grid grid-cols-3 gap-4">
        {[
          { label: 'Total Agents', value: agents.length, color: 'text-indigo-400' },
          { label: 'Active Agents', value: agents.filter(a => a.active).length, color: 'text-emerald-400' },
          { label: 'Inactive', value: agents.filter(a => !a.active).length, color: 'text-rose-400' },
        ].map((s, i) => (
          <div key={i} className="glass-card p-4 text-center">
            <p className="text-xs text-slate-400 uppercase tracking-wider">{s.label}</p>
            <p className={`text-2xl font-bold mt-1 ${s.color}`}>{s.value}</p>
          </div>
        ))}
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading agents...</div>
      ) : agents.length === 0 ? (
        <div className="glass-card p-12 text-center">
          <UserCheck className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <p className="text-slate-400">No agents registered yet. Add your first agent!</p>
        </div>
      ) : (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Agent</th>
                <th className="px-6 py-4">Code & License</th>
                <th className="px-6 py-4">Specialization</th>
                <th className="px-6 py-4">Region</th>
                <th className="px-6 py-4">Commission</th>
                <th className="px-6 py-4">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {agents.map((a) => (
                <tr key={a.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-600 flex items-center justify-center font-bold text-white text-sm">
                        {a.firstName?.charAt(0)}{a.lastName?.charAt(0)}
                      </div>
                      <div>
                        <p className="font-semibold text-slate-100">{a.firstName} {a.lastName}</p>
                        <p className="text-xs text-slate-400">{a.email}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <p className="font-mono text-xs text-indigo-400 font-semibold">{a.agentCode}</p>
                    <p className="text-xs text-slate-500 mt-0.5">{a.licenseNumber}</p>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-purple-500/20 text-purple-400 border border-purple-500/30">
                      {a.specialization}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1.5 text-xs text-slate-400">
                      <MapPin className="w-3.5 h-3.5 text-amber-400" />
                      {a.assignedRegion || '—'}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1 text-sm font-semibold text-emerald-400">
                      <Percent className="w-3.5 h-3.5" />
                      {a.commissionRate}
                    </div>
                    <p className="text-xs text-slate-500 mt-0.5">Earned: ₹{a.totalCommission || 0}</p>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${a.active ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30' : 'bg-rose-500/20 text-rose-400 border-rose-500/30'}`}>
                      {a.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Create Agent Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="glass-card max-w-lg w-full p-6 space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-bold text-slate-100">Register New Agent</h3>
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
              <input type="email" placeholder="Email" required className="glass-input w-full" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
              <input type="password" placeholder="Password" required className="glass-input w-full" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
              <input type="text" placeholder="License Number (e.g. LIC-12345)" required className="glass-input w-full" value={form.licenseNumber} onChange={e => setForm({ ...form, licenseNumber: e.target.value })} />
              <div className="grid grid-cols-2 gap-3">
                <select className="glass-input" value={form.specialization} onChange={e => setForm({ ...form, specialization: e.target.value })}>
                  {SPECIALIZATIONS.map(s => <option key={s} value={s}>{s}</option>)}
                </select>
                <input type="text" placeholder="Assigned Region" className="glass-input" value={form.assignedRegion} onChange={e => setForm({ ...form, assignedRegion: e.target.value })} />
              </div>
              <input type="number" step="0.1" placeholder="Commission Rate (%)" className="glass-input w-full" value={form.commissionRate} onChange={e => setForm({ ...form, commissionRate: e.target.value })} />
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
                <button type="submit" className="btn-primary flex items-center gap-2">
                  <CheckCircle className="w-4 h-4" />
                  Create Agent
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

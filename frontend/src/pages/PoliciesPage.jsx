import React, { useEffect, useState } from 'react';
import { policyApi, customerApi } from '../services/api';
import { FileText, Plus, Shield } from 'lucide-react';

export const PoliciesPage = () => {
  const [policies, setPolicies] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    policyName: '', policyType: 'HEALTH', coverageAmount: '500000', premiumAmount: '12000',
    startDate: '', endDate: '', customerId: ''
  });

  useEffect(() => {
    fetchPolicies();
    fetchCustomers();
  }, []);

  const fetchPolicies = async () => {
    try {
      const res = await policyApi.getAll();
      setPolicies(res.data.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const fetchCustomers = async () => {
    try {
      const res = await customerApi.getAll();
      setCustomers(res.data.data);
    } catch (e) {}
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await policyApi.create(formData);
      setShowModal(false);
      fetchPolicies();
    } catch (err) {
      alert(err.response?.data?.message || 'Error creating policy');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Insurance Policies</h1>
          <p className="text-sm text-slate-400">Issue and manage policy coverage</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          New Policy
        </button>
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading policies...</div>
      ) : (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Policy No & Name</th>
                <th className="px-6 py-4">Customer</th>
                <th className="px-6 py-4">Coverage</th>
                <th className="px-6 py-4">Annual Premium</th>
                <th className="px-6 py-4">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {policies.map((p) => (
                <tr key={p.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4">
                    <div className="font-semibold text-slate-100">{p.policyName}</div>
                    <div className="text-xs text-indigo-400 font-mono">{p.policyNumber}</div>
                  </td>
                  <td className="px-6 py-4 text-slate-300">
                    {p.customerFullName || `Customer #${p.customerId}`}
                  </td>
                  <td className="px-6 py-4 font-semibold text-emerald-400">
                    ₹{p.coverageAmount}
                  </td>
                  <td className="px-6 py-4 font-semibold text-indigo-300">
                    ₹{p.premiumAmount}
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
                      {p.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="glass-card max-w-lg w-full p-6 space-y-4">
            <h3 className="text-lg font-bold text-slate-100">Issue New Insurance Policy</h3>
            <form onSubmit={handleCreate} className="space-y-3">
              <select required className="glass-input w-full" onChange={e => setFormData({...formData, customerId: e.target.value})}>
                <option value="">Select Customer</option>
                {customers.map(c => (
                  <option key={c.id} value={c.id}>{c.firstName} {c.lastName} ({c.email})</option>
                ))}
              </select>
              <input type="text" placeholder="Policy Name" required className="glass-input w-full" onChange={e => setFormData({...formData, policyName: e.target.value})} />
              <select className="glass-input w-full" onChange={e => setFormData({...formData, policyType: e.target.value})}>
                <option value="HEALTH">Health Insurance</option>
                <option value="LIFE">Life Insurance</option>
                <option value="VEHICLE">Vehicle Insurance</option>
                <option value="HOME">Home Insurance</option>
              </select>
              <div className="grid grid-cols-2 gap-3">
                <input type="number" placeholder="Coverage Amount (₹)" required className="glass-input" onChange={e => setFormData({...formData, coverageAmount: e.target.value})} />
                <input type="number" placeholder="Premium Amount (₹)" required className="glass-input" onChange={e => setFormData({...formData, premiumAmount: e.target.value})} />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-xs text-slate-400">Start Date</label>
                  <input type="date" required className="glass-input w-full" onChange={e => setFormData({...formData, startDate: e.target.value})} />
                </div>
                <div>
                  <label className="text-xs text-slate-400">End Date</label>
                  <input type="date" required className="glass-input w-full" onChange={e => setFormData({...formData, endDate: e.target.value})} />
                </div>
              </div>
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
                <button type="submit" className="btn-primary">Issue Policy</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

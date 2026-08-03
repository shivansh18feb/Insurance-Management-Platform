import React, { useEffect, useState } from 'react';
import { claimApi, policyApi } from '../services/api';
import { AlertCircle, Plus, CheckCircle, XCircle } from 'lucide-react';

export const ClaimsPage = () => {
  const [claims, setClaims] = useState([]);
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    policyId: '', claimAmount: '', claimReason: '', claimDate: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    fetchClaims();
    fetchPolicies();
  }, []);

  const fetchClaims = async () => {
    try {
      const res = await claimApi.getAll();
      setClaims(res.data.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const fetchPolicies = async () => {
    try {
      const res = await policyApi.getAll();
      setPolicies(res.data.data);
    } catch (e) {}
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await claimApi.create(formData);
      setShowModal(false);
      fetchClaims();
    } catch (err) {
      alert(err.response?.data?.message || 'Error submitting claim');
    }
  };

  const handleApprove = async (claimId, amount) => {
    const approvedAmount = prompt('Enter approved settlement amount (₹):', amount);
    if (!approvedAmount) return;
    try {
      await claimApi.approve(claimId, { approvedAmount });
      fetchClaims();
    } catch (err) {
      alert(err.response?.data?.message || 'Error approving claim');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Claim Requests & Settlement</h1>
          <p className="text-sm text-slate-400">File and verify insurance claim requests</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Submit Claim
        </button>
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading claims...</div>
      ) : (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Claim Number</th>
                <th className="px-6 py-4">Reason</th>
                <th className="px-6 py-4">Claimed / Approved Amount</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {claims.map((c) => (
                <tr key={c.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4 font-mono text-indigo-400 font-semibold">
                    {c.claimNumber}
                  </td>
                  <td className="px-6 py-4 text-slate-300">
                    {c.claimReason}
                  </td>
                  <td className="px-6 py-4">
                    <div className="font-semibold text-slate-100">₹{c.claimAmount}</div>
                    {c.approvedAmount && (
                      <div className="text-xs text-emerald-400">Approved: ₹{c.approvedAmount}</div>
                    )}
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${
                      c.status === 'APPROVED' || c.status === 'SETTLED' ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30' :
                      c.status === 'REJECTED' ? 'bg-rose-500/20 text-rose-400 border-rose-500/30' :
                      'bg-amber-500/20 text-amber-400 border-amber-500/30'
                    }`}>
                      {c.status}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    {c.status === 'SUBMITTED' && (
                      <button 
                        onClick={() => handleApprove(c.id, c.claimAmount)}
                        className="btn-secondary text-xs flex items-center gap-1 text-emerald-400 hover:text-emerald-300"
                      >
                        <CheckCircle className="w-3.5 h-3.5" />
                        Approve
                      </button>
                    )}
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
            <h3 className="text-lg font-bold text-slate-100">File Insurance Claim</h3>
            <form onSubmit={handleCreate} className="space-y-3">
              <select required className="glass-input w-full" onChange={e => setFormData({...formData, policyId: e.target.value})}>
                <option value="">Select Policy</option>
                {policies.map(p => (
                  <option key={p.id} value={p.id}>{p.policyName} ({p.policyNumber})</option>
                ))}
              </select>
              <input type="number" placeholder="Claim Amount (₹)" required className="glass-input w-full" onChange={e => setFormData({...formData, claimAmount: e.target.value})} />
              <textarea placeholder="Reason for claim..." required className="glass-input w-full h-24" onChange={e => setFormData({...formData, claimReason: e.target.value})}></textarea>
              <input type="date" required className="glass-input w-full" value={formData.claimDate} onChange={e => setFormData({...formData, claimDate: e.target.value})} />
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
                <button type="submit" className="btn-primary">Submit Claim</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

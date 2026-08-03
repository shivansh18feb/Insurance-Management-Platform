import React, { useState } from 'react';
import { premiumApi } from '../services/api';
import { CreditCard, Search, CheckCircle } from 'lucide-react';

export const PremiumsPage = () => {
  const [policyId, setPolicyId] = useState('');
  const [schedules, setSchedules] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchSchedule = async (e) => {
    e.preventDefault();
    if (!policyId) return;
    setLoading(true);
    try {
      const res = await premiumApi.getSchedule(policyId);
      setSchedules(res.data.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error fetching schedule');
    } finally {
      setLoading(false);
    }
  };

  const handlePay = async (scheduleId) => {
    try {
      await premiumApi.pay(scheduleId, 'UPI', 'Paid via web dashboard');
      fetchSchedule({ preventDefault: () => {} });
    } catch (err) {
      alert(err.response?.data?.message || 'Payment failed');
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Premium Installment Tracking</h1>
        <p className="text-sm text-slate-400">Track and pay upcoming policy premium schedule</p>
      </div>

      <div className="glass-card p-6">
        <form onSubmit={fetchSchedule} className="flex gap-4 items-center">
          <input 
            type="number" 
            placeholder="Enter Policy ID..." 
            value={policyId}
            onChange={(e) => setPolicyId(e.target.value)}
            className="glass-input flex-1" 
            required 
          />
          <button type="submit" className="btn-primary flex items-center gap-2">
            <Search className="w-4 h-4" />
            Lookup Schedule
          </button>
        </form>
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Fetching premium schedule...</div>
      ) : schedules.length > 0 ? (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Installment #</th>
                <th className="px-6 py-4">Premium Number</th>
                <th className="px-6 py-4">Due Date</th>
                <th className="px-6 py-4">Amount</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {schedules.map((s) => (
                <tr key={s.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4 font-semibold text-slate-100">#{s.installmentNumber}</td>
                  <td className="px-6 py-4 font-mono text-indigo-400">{s.premiumNumber}</td>
                  <td className="px-6 py-4 text-slate-300">{s.dueDate}</td>
                  <td className="px-6 py-4 font-semibold text-emerald-400">₹{s.amount}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${
                      s.status === 'PAID' ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30' :
                      s.status === 'OVERDUE' ? 'bg-rose-500/20 text-rose-400 border-rose-500/30' :
                      'bg-amber-500/20 text-amber-400 border-amber-500/30'
                    }`}>
                      {s.status}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    {s.status !== 'PAID' && (
                      <button onClick={() => handlePay(s.id)} className="btn-primary text-xs flex items-center gap-1">
                        <CreditCard className="w-3.5 h-3.5" />
                        Pay Now
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : null}
    </div>
  );
};

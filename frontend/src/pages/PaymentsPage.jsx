import React, { useEffect, useState } from 'react';
import { paymentApi } from '../services/api';
import { CreditCard, Plus, CheckCircle, Clock, XCircle, TrendingUp, X } from 'lucide-react';

const PAYMENT_METHODS = ['CASH', 'CHEQUE', 'ONLINE', 'CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NEFT', 'RTGS'];
const STATUS_COLORS = {
  COMPLETED: 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30',
  PENDING: 'bg-amber-500/20 text-amber-400 border-amber-500/30',
  FAILED: 'bg-rose-500/20 text-rose-400 border-rose-500/30',
  REFUNDED: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
};

export const PaymentsPage = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    policyId: '', paymentAmount: '', paymentMethod: 'ONLINE', transactionId: '', remarks: ''
  });

  useEffect(() => { fetchPayments(); }, []);

  const fetchPayments = async () => {
    try {
      const res = await paymentApi.getAll();
      setPayments(res.data.data || []);
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
      await paymentApi.create({ ...form, policyId: parseInt(form.policyId), paymentAmount: parseFloat(form.paymentAmount) });
      setShowModal(false);
      setForm({ policyId: '', paymentAmount: '', paymentMethod: 'ONLINE', transactionId: '', remarks: '' });
      fetchPayments();
    } catch (err) {
      setError(err.response?.data?.message || 'Error recording payment');
    }
  };

  const totalRevenue = payments.filter(p => p.paymentStatus === 'COMPLETED')
    .reduce((sum, p) => sum + (p.paymentAmount || 0), 0);
  const pending = payments.filter(p => p.paymentStatus === 'PENDING').length;
  const failed = payments.filter(p => p.paymentStatus === 'FAILED').length;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Payment Management</h1>
          <p className="text-sm text-slate-400">Track and record premium and claim payments</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Record Payment
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-4 gap-4">
        {[
          { label: 'Total Payments', value: payments.length, icon: CreditCard, color: 'from-blue-500 to-indigo-600' },
          { label: 'Total Revenue', value: `₹${totalRevenue.toLocaleString('en-IN')}`, icon: TrendingUp, color: 'from-emerald-500 to-teal-600' },
          { label: 'Pending', value: pending, icon: Clock, color: 'from-amber-500 to-orange-600' },
          { label: 'Failed', value: failed, icon: XCircle, color: 'from-rose-500 to-pink-600' },
        ].map((kpi, i) => {
          const Icon = kpi.icon;
          return (
            <div key={i} className="glass-card p-5 flex items-center justify-between">
              <div>
                <p className="text-xs font-semibold uppercase tracking-wider text-slate-400">{kpi.label}</p>
                <p className="text-xl font-bold text-slate-100 mt-1">{kpi.value}</p>
              </div>
              <div className={`p-3 rounded-xl bg-gradient-to-tr ${kpi.color} text-white`}>
                <Icon className="w-5 h-5" />
              </div>
            </div>
          );
        })}
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading payments...</div>
      ) : payments.length === 0 ? (
        <div className="glass-card p-12 text-center">
          <CreditCard className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <p className="text-slate-400">No payments recorded yet.</p>
        </div>
      ) : (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Payment ID</th>
                <th className="px-6 py-4">Policy</th>
                <th className="px-6 py-4">Amount</th>
                <th className="px-6 py-4">Method</th>
                <th className="px-6 py-4">Transaction ID</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Date</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {payments.map((p) => (
                <tr key={p.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4 font-mono text-xs text-indigo-400 font-semibold">#{p.id}</td>
                  <td className="px-6 py-4 text-xs text-slate-400">Policy #{p.policyId}</td>
                  <td className="px-6 py-4 font-semibold text-emerald-400">₹{(p.paymentAmount || 0).toLocaleString('en-IN')}</td>
                  <td className="px-6 py-4">
                    <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-slate-800 text-slate-300 border border-slate-700">
                      {p.paymentMethod}
                    </span>
                  </td>
                  <td className="px-6 py-4 font-mono text-xs text-slate-400">{p.transactionId || '—'}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${STATUS_COLORS[p.paymentStatus] || STATUS_COLORS.PENDING}`}>
                      {p.paymentStatus || 'PENDING'}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-xs text-slate-400">
                    {p.createdAt ? new Date(p.createdAt).toLocaleDateString('en-IN') : '—'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Record Payment Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="glass-card max-w-md w-full p-6 space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-bold text-slate-100">Record Payment</h3>
              <button onClick={() => setShowModal(false)} className="p-1.5 hover:bg-slate-800 rounded-lg text-slate-400">
                <X className="w-4 h-4" />
              </button>
            </div>
            {error && <div className="bg-rose-500/10 border border-rose-500/30 rounded-xl px-4 py-3 text-sm text-rose-400">{error}</div>}
            <form onSubmit={handleCreate} className="space-y-3">
              <input type="number" placeholder="Policy ID" required className="glass-input w-full" value={form.policyId} onChange={e => setForm({ ...form, policyId: e.target.value })} />
              <input type="number" step="0.01" placeholder="Payment Amount (₹)" required className="glass-input w-full" value={form.paymentAmount} onChange={e => setForm({ ...form, paymentAmount: e.target.value })} />
              <select className="glass-input w-full" value={form.paymentMethod} onChange={e => setForm({ ...form, paymentMethod: e.target.value })}>
                {PAYMENT_METHODS.map(m => <option key={m} value={m}>{m}</option>)}
              </select>
              <input type="text" placeholder="Transaction ID (optional)" className="glass-input w-full" value={form.transactionId} onChange={e => setForm({ ...form, transactionId: e.target.value })} />
              <textarea placeholder="Remarks (optional)" rows={2} className="glass-input w-full resize-none" value={form.remarks} onChange={e => setForm({ ...form, remarks: e.target.value })} />
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
                <button type="submit" className="btn-primary flex items-center gap-2">
                  <CheckCircle className="w-4 h-4" />
                  Record Payment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

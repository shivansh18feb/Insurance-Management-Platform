import React, { useEffect, useState } from 'react';
import { customerApi } from '../services/api';
import { Users, Plus, Phone, Mail, MapPin, Trash2 } from 'lucide-react';

export const CustomersPage = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', phoneNumber: '',
    dateOfBirth: '', gender: 'MALE', address: '', city: '', state: '', postalCode: '400001'
  });

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const res = await customerApi.getAll();
      setCustomers(res.data.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await customerApi.create(formData);
      setShowModal(false);
      fetchCustomers();
    } catch (err) {
      alert(err.response?.data?.message || 'Error creating customer');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Customer Management</h1>
          <p className="text-sm text-slate-400">Register and manage customer profiles</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Add Customer
        </button>
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading customers...</div>
      ) : (
        <div className="glass-card overflow-hidden">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">Customer Name</th>
                <th className="px-6 py-4">Contact Info</th>
                <th className="px-6 py-4">Location</th>
                <th className="px-6 py-4">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {customers.map((c) => (
                <tr key={c.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4 font-semibold text-slate-100">
                    {c.firstName} {c.lastName}
                  </td>
                  <td className="px-6 py-4 space-y-1">
                    <div className="flex items-center gap-2 text-xs text-slate-400">
                      <Mail className="w-3.5 h-3.5 text-indigo-400" />
                      {c.email}
                    </div>
                    <div className="flex items-center gap-2 text-xs text-slate-400">
                      <Phone className="w-3.5 h-3.5 text-emerald-400" />
                      {c.phoneNumber}
                    </div>
                  </td>
                  <td className="px-6 py-4 text-xs text-slate-400">
                    <div className="flex items-center gap-1.5">
                      <MapPin className="w-3.5 h-3.5 text-amber-400" />
                      {c.city}, {c.state}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                      {c.status || 'ACTIVE'}
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
            <h3 className="text-lg font-bold text-slate-100">Register New Customer</h3>
            <form onSubmit={handleCreate} className="space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <input type="text" placeholder="First Name" required className="glass-input" onChange={e => setFormData({...formData, firstName: e.target.value})} />
                <input type="text" placeholder="Last Name" required className="glass-input" onChange={e => setFormData({...formData, lastName: e.target.value})} />
              </div>
              <input type="email" placeholder="Email" required className="glass-input w-full" onChange={e => setFormData({...formData, email: e.target.value})} />
              <input type="text" placeholder="10-digit Phone" required className="glass-input w-full" onChange={e => setFormData({...formData, phoneNumber: e.target.value})} />
              <div className="grid grid-cols-2 gap-3">
                <input type="date" required className="glass-input" onChange={e => setFormData({...formData, dateOfBirth: e.target.value})} />
                <select className="glass-input" onChange={e => setFormData({...formData, gender: e.target.value})}>
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                </select>
              </div>
              <input type="text" placeholder="Address" required className="glass-input w-full" onChange={e => setFormData({...formData, address: e.target.value})} />
              <div className="grid grid-cols-3 gap-2">
                <input type="text" placeholder="City" required className="glass-input" onChange={e => setFormData({...formData, city: e.target.value})} />
                <input type="text" placeholder="State" required className="glass-input" onChange={e => setFormData({...formData, state: e.target.value})} />
                <input type="text" placeholder="PIN" required className="glass-input" onChange={e => setFormData({...formData, postalCode: e.target.value})} />
              </div>
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
                <button type="submit" className="btn-primary">Save Customer</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

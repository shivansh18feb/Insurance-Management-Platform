import React, { useEffect, useState } from 'react';
import { adminApi } from '../services/api';
import { UserCog, Lock, Unlock, Shield, UserX, RefreshCw, ChevronDown } from 'lucide-react';

const ROLES = ['ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER'];

export const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [roleMenuOpen, setRoleMenuOpen] = useState(null);

  useEffect(() => { fetchUsers(); }, []);

  const fetchUsers = async () => {
    try {
      const res = await adminApi.getUsers();
      setUsers(res.data.data || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const toggleStatus = async (userId) => {
    try {
      await adminApi.toggleUserStatus(userId);
      fetchUsers();
    } catch (e) {
      alert(e.response?.data?.message || 'Error toggling status');
    }
  };

  const changeRole = async (userId, role) => {
    try {
      await adminApi.updateUserRole(userId, role);
      setRoleMenuOpen(null);
      fetchUsers();
    } catch (e) {
      alert(e.response?.data?.message || 'Error changing role');
    }
  };

  const unlockUser = async (userId) => {
    try {
      await adminApi.unlockUser(userId);
      fetchUsers();
    } catch (e) {
      alert(e.response?.data?.message || 'Error unlocking user');
    }
  };

  const roleColors = {
    ADMIN: 'bg-rose-500/20 text-rose-400 border-rose-500/30',
    EMPLOYEE: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
    AGENT: 'bg-purple-500/20 text-purple-400 border-purple-500/30',
    CUSTOMER: 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30',
  };

  const stats = [
    { label: 'Total Users', value: users.length },
    { label: 'Admins', value: users.filter(u => u.role === 'ADMIN').length },
    { label: 'Locked', value: users.filter(u => u.accountLocked).length },
    { label: 'Disabled', value: users.filter(u => !u.enabled).length },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">User Management</h1>
          <p className="text-sm text-slate-400">Manage system users, roles, and access control</p>
        </div>
        <button onClick={fetchUsers} className="btn-secondary flex items-center gap-2">
          <RefreshCw className="w-4 h-4" />
          Refresh
        </button>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-4 gap-4">
        {stats.map((s, i) => (
          <div key={i} className="glass-card p-4 text-center">
            <p className="text-xs text-slate-400 uppercase tracking-wider">{s.label}</p>
            <p className="text-2xl font-bold text-slate-100 mt-1">{s.value}</p>
          </div>
        ))}
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading users...</div>
      ) : (
        <div className="glass-card overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-800 flex items-center gap-2">
            <UserCog className="w-5 h-5 text-indigo-400" />
            <span className="font-semibold text-slate-200">System Users ({users.length})</span>
          </div>
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-900/90 text-xs font-semibold text-slate-400 uppercase tracking-wider border-b border-slate-800">
              <tr>
                <th className="px-6 py-4">User</th>
                <th className="px-6 py-4">Email</th>
                <th className="px-6 py-4">Role</th>
                <th className="px-6 py-4">Account Status</th>
                <th className="px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {users.map((u) => (
                <tr key={u.id} className="hover:bg-slate-800/40 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-slate-600 to-slate-700 flex items-center justify-center text-xs font-bold text-white">
                        {u.firstName?.charAt(0)}{u.lastName?.charAt(0)}
                      </div>
                      <span className="font-semibold text-slate-100">{u.firstName} {u.lastName}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-slate-400 text-xs">{u.email}</td>
                  <td className="px-6 py-4 relative">
                    <button
                      onClick={() => setRoleMenuOpen(roleMenuOpen === u.id ? null : u.id)}
                      className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold border transition-all hover:opacity-80 ${roleColors[u.role] || 'bg-slate-500/20 text-slate-400 border-slate-500/30'}`}
                    >
                      {u.role}
                      <ChevronDown className="w-3 h-3" />
                    </button>
                    {roleMenuOpen === u.id && (
                      <div className="absolute left-6 top-full mt-1 z-50 bg-slate-900 border border-slate-700 rounded-xl shadow-xl overflow-hidden">
                        {ROLES.filter(r => r !== u.role).map(role => (
                          <button
                            key={role}
                            onClick={() => changeRole(u.id, role)}
                            className="w-full text-left px-4 py-2 text-sm hover:bg-slate-800 text-slate-300 transition-colors"
                          >
                            Set as {role}
                          </button>
                        ))}
                      </div>
                    )}
                  </td>
                  <td className="px-6 py-4">
                    <div className="space-y-1">
                      <span className={`inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold border ${u.enabled ? 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30' : 'bg-rose-500/20 text-rose-400 border-rose-500/30'}`}>
                        {u.enabled ? 'Active' : 'Disabled'}
                      </span>
                      {u.accountLocked && (
                        <span className="block px-2.5 py-0.5 rounded-full text-xs font-semibold border bg-amber-500/20 text-amber-400 border-amber-500/30">
                          🔒 Locked
                        </span>
                      )}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => toggleStatus(u.id)}
                        title={u.enabled ? 'Disable account' : 'Enable account'}
                        className={`p-1.5 rounded-lg border transition-all ${u.enabled ? 'text-rose-400 border-rose-500/30 hover:bg-rose-500/10' : 'text-emerald-400 border-emerald-500/30 hover:bg-emerald-500/10'}`}
                      >
                        {u.enabled ? <UserX className="w-4 h-4" /> : <Shield className="w-4 h-4" />}
                      </button>
                      {u.accountLocked && (
                        <button
                          onClick={() => unlockUser(u.id)}
                          title="Unlock account"
                          className="p-1.5 rounded-lg border text-amber-400 border-amber-500/30 hover:bg-amber-500/10 transition-all"
                        >
                          <Unlock className="w-4 h-4" />
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../services/api';
import { Settings, Lock, User, Shield, Bell, Database, CheckCircle, Eye, EyeOff } from 'lucide-react';

export const SettingsPage = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');
  const [passwordForm, setPasswordForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [showPass, setShowPass] = useState({ current: false, new: false, confirm: false });
  const [pwSuccess, setPwSuccess] = useState('');
  const [pwError, setPwError] = useState('');
  const [notifSettings, setNotifSettings] = useState({
    emailPolicyExpiry: true,
    emailPremiumDue: true,
    emailClaimUpdate: true,
    emailPaymentConfirm: true,
  });

  const tabs = [
    { id: 'profile', label: 'Profile', icon: User },
    { id: 'security', label: 'Security', icon: Lock },
    { id: 'notifications', label: 'Notifications', icon: Bell },
    { id: 'system', label: 'System', icon: Database },
  ];

  const handleChangePassword = async (e) => {
    e.preventDefault();
    setPwError('');
    setPwSuccess('');
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      setPwError('New passwords do not match');
      return;
    }
    if (passwordForm.newPassword.length < 8) {
      setPwError('Password must be at least 8 characters');
      return;
    }
    try {
      await authApi.changePassword({ currentPassword: passwordForm.currentPassword, newPassword: passwordForm.newPassword });
      setPwSuccess('Password changed successfully!');
      setPasswordForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      setPwError(err.response?.data?.message || 'Failed to change password');
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-3">
          <Settings className="w-7 h-7 text-indigo-400" />
          Settings
        </h1>
        <p className="text-sm text-slate-400">Manage your account and system preferences</p>
      </div>

      <div className="flex gap-8">
        {/* Sidebar Tabs */}
        <div className="w-52 flex-shrink-0">
          <nav className="space-y-1">
            {tabs.map(tab => {
              const Icon = tab.icon;
              return (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={`w-full flex items-center gap-3 px-4 py-2.5 rounded-xl text-sm font-medium transition-all ${
                    activeTab === tab.id
                      ? 'bg-indigo-600/20 text-indigo-400 border border-indigo-500/30'
                      : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  {tab.label}
                </button>
              );
            })}
          </nav>
        </div>

        {/* Tab Content */}
        <div className="flex-1 min-w-0">
          {/* Profile Tab */}
          {activeTab === 'profile' && (
            <div className="glass-card p-6 space-y-6">
              <h2 className="text-lg font-bold text-slate-100">Profile Information</h2>
              <div className="flex items-center gap-5">
                <div className="w-20 h-20 rounded-2xl bg-gradient-to-tr from-indigo-500 to-purple-600 flex items-center justify-center font-bold text-white text-3xl shadow-xl shadow-indigo-500/20">
                  {user?.email?.charAt(0).toUpperCase() || 'U'}
                </div>
                <div>
                  <p className="text-lg font-bold text-slate-100">{user?.email}</p>
                  <span className="inline-block mt-1 text-xs uppercase font-bold tracking-wider px-3 py-1 rounded-full bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
                    {user?.role}
                  </span>
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4 pt-2">
                <div className="space-y-1">
                  <p className="text-xs text-slate-500 uppercase tracking-wider">Email</p>
                  <p className="text-sm font-medium text-slate-200">{user?.email}</p>
                </div>
                <div className="space-y-1">
                  <p className="text-xs text-slate-500 uppercase tracking-wider">Role</p>
                  <p className="text-sm font-medium text-slate-200">{user?.role}</p>
                </div>
              </div>
              <div className="border-t border-slate-800 pt-4">
                <div className="flex items-center gap-2 text-xs text-slate-500">
                  <Shield className="w-3.5 h-3.5" />
                  Account secured with JWT authentication
                </div>
              </div>
            </div>
          )}

          {/* Security Tab */}
          {activeTab === 'security' && (
            <div className="glass-card p-6 space-y-6">
              <h2 className="text-lg font-bold text-slate-100">Change Password</h2>
              {pwSuccess && (
                <div className="bg-emerald-500/10 border border-emerald-500/30 rounded-xl px-4 py-3 flex items-center gap-2 text-sm text-emerald-400">
                  <CheckCircle className="w-4 h-4" />
                  {pwSuccess}
                </div>
              )}
              {pwError && (
                <div className="bg-rose-500/10 border border-rose-500/30 rounded-xl px-4 py-3 text-sm text-rose-400">{pwError}</div>
              )}
              <form onSubmit={handleChangePassword} className="space-y-4 max-w-md">
                {[
                  { field: 'currentPassword', label: 'Current Password', key: 'current' },
                  { field: 'newPassword', label: 'New Password', key: 'new' },
                  { field: 'confirmPassword', label: 'Confirm New Password', key: 'confirm' },
                ].map(({ field, label, key }) => (
                  <div key={field} className="space-y-1.5">
                    <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">{label}</label>
                    <div className="relative">
                      <input
                        type={showPass[key] ? 'text' : 'password'}
                        required
                        className="glass-input w-full pr-10"
                        value={passwordForm[field]}
                        onChange={e => setPasswordForm({ ...passwordForm, [field]: e.target.value })}
                      />
                      <button
                        type="button"
                        onClick={() => setShowPass({ ...showPass, [key]: !showPass[key] })}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-200"
                      >
                        {showPass[key] ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                      </button>
                    </div>
                  </div>
                ))}
                <button type="submit" className="btn-primary mt-2">Update Password</button>
              </form>
              <div className="border-t border-slate-800 pt-6 space-y-3">
                <h3 className="text-sm font-semibold text-slate-300">Security Policies</h3>
                {[
                  'Passwords must be at least 8 characters long',
                  'Accounts are locked after 5 consecutive failed login attempts',
                  'JWT tokens expire after 24 hours',
                  'Refresh tokens are rotated on every use',
                ].map((policy, i) => (
                  <div key={i} className="flex items-start gap-2 text-xs text-slate-400">
                    <Shield className="w-3.5 h-3.5 text-indigo-400 flex-shrink-0 mt-0.5" />
                    {policy}
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Notifications Tab */}
          {activeTab === 'notifications' && (
            <div className="glass-card p-6 space-y-6">
              <h2 className="text-lg font-bold text-slate-100">Notification Preferences</h2>
              <div className="space-y-4">
                {[
                  { key: 'emailPolicyExpiry', label: 'Policy Expiry Alerts', desc: 'Get notified 30 days before policy expires' },
                  { key: 'emailPremiumDue', label: 'Premium Due Reminders', desc: 'Reminders for upcoming premium payments' },
                  { key: 'emailClaimUpdate', label: 'Claim Status Updates', desc: 'Notify when claim status changes' },
                  { key: 'emailPaymentConfirm', label: 'Payment Confirmations', desc: 'Receipt notifications for successful payments' },
                ].map(({ key, label, desc }) => (
                  <div key={key} className="flex items-center justify-between py-3 border-b border-slate-800/80">
                    <div>
                      <p className="text-sm font-medium text-slate-200">{label}</p>
                      <p className="text-xs text-slate-500 mt-0.5">{desc}</p>
                    </div>
                    <button
                      onClick={() => setNotifSettings({ ...notifSettings, [key]: !notifSettings[key] })}
                      className={`w-12 h-6 rounded-full transition-all relative ${notifSettings[key] ? 'bg-indigo-600' : 'bg-slate-700'}`}
                    >
                      <span className={`absolute top-1 w-4 h-4 rounded-full bg-white shadow transition-all ${notifSettings[key] ? 'left-7' : 'left-1'}`} />
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* System Tab */}
          {activeTab === 'system' && (
            <div className="glass-card p-6 space-y-6">
              <h2 className="text-lg font-bold text-slate-100">System Information</h2>
              <div className="grid grid-cols-2 gap-4">
                {[
                  { label: 'Application', value: 'InsurTech Enterprise Platform' },
                  { label: 'Version', value: '1.0.0' },
                  { label: 'Backend Framework', value: 'Spring Boot 3.x' },
                  { label: 'Database', value: 'PostgreSQL' },
                  { label: 'Authentication', value: 'JWT + Refresh Token' },
                  { label: 'Frontend', value: 'React + Vite + Tailwind' },
                  { label: 'API Documentation', value: 'Swagger / OpenAPI 3' },
                  { label: 'Build Status', value: '✅ Production Ready' },
                ].map(({ label, value }) => (
                  <div key={label} className="bg-slate-900/60 rounded-xl p-4 border border-slate-800">
                    <p className="text-xs text-slate-500 uppercase tracking-wider mb-1">{label}</p>
                    <p className="text-sm font-semibold text-slate-200">{value}</p>
                  </div>
                ))}
              </div>
              <div className="border-t border-slate-800 pt-4">
                <p className="text-xs text-slate-500">
                  API Base URL: <span className="font-mono text-indigo-400">http://localhost:8080</span>
                  &nbsp;&nbsp;|&nbsp;&nbsp;
                  Swagger UI: <a href="http://localhost:8080/swagger-ui/index.html" target="_blank" rel="noreferrer" className="text-indigo-400 underline hover:text-indigo-300">Open Docs</a>
                </p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

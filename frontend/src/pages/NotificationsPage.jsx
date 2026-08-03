import React, { useEffect, useState } from 'react';
import { notificationApi } from '../services/api';
import { Bell, CheckCheck, Info, AlertTriangle, CheckCircle, FileText, CreditCard, RefreshCw } from 'lucide-react';

const TYPE_ICONS = {
  POLICY_EXPIRY: FileText,
  PREMIUM_DUE: CreditCard,
  CLAIM_UPDATE: AlertTriangle,
  GENERAL: Info,
  PAYMENT_CONFIRMATION: CheckCircle,
};

const TYPE_COLORS = {
  POLICY_EXPIRY: 'text-amber-400 bg-amber-500/10 border-amber-500/20',
  PREMIUM_DUE: 'text-rose-400 bg-rose-500/10 border-rose-500/20',
  CLAIM_UPDATE: 'text-blue-400 bg-blue-500/10 border-blue-500/20',
  GENERAL: 'text-slate-400 bg-slate-500/10 border-slate-500/20',
  PAYMENT_CONFIRMATION: 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20',
};

export const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => { fetchNotifications(); }, []);

  const fetchNotifications = async () => {
    try {
      const res = await notificationApi.getAll();
      setNotifications(res.data.data || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const markAllRead = async () => {
    try {
      await notificationApi.markAllRead();
      fetchNotifications();
    } catch (e) {
      console.error(e);
    }
  };

  const markRead = async (id) => {
    try {
      await notificationApi.markRead(id);
      fetchNotifications();
    } catch (e) {
      console.error(e);
    }
  };

  const filtered = filter === 'ALL' ? notifications :
    filter === 'UNREAD' ? notifications.filter(n => !n.read) :
    notifications.filter(n => n.notificationType === filter);

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-3">
            Notifications
            {unreadCount > 0 && (
              <span className="bg-indigo-600 text-white text-xs font-bold px-2.5 py-1 rounded-full">{unreadCount}</span>
            )}
          </h1>
          <p className="text-sm text-slate-400">System alerts, policy updates, and reminders</p>
        </div>
        <div className="flex items-center gap-3">
          <button onClick={fetchNotifications} className="btn-secondary flex items-center gap-2">
            <RefreshCw className="w-4 h-4" />
            Refresh
          </button>
          {unreadCount > 0 && (
            <button onClick={markAllRead} className="btn-primary flex items-center gap-2">
              <CheckCheck className="w-4 h-4" />
              Mark All Read
            </button>
          )}
        </div>
      </div>

      {/* Filter Tabs */}
      <div className="flex items-center gap-2 flex-wrap">
        {['ALL', 'UNREAD', 'POLICY_EXPIRY', 'PREMIUM_DUE', 'CLAIM_UPDATE', 'PAYMENT_CONFIRMATION'].map(tab => (
          <button
            key={tab}
            onClick={() => setFilter(tab)}
            className={`px-4 py-1.5 rounded-full text-xs font-semibold border transition-all ${
              filter === tab
                ? 'bg-indigo-600/30 border-indigo-500/60 text-indigo-300'
                : 'bg-slate-900/60 border-slate-800 text-slate-400 hover:text-slate-200'
            }`}
          >
            {tab.replace('_', ' ')}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="text-slate-400 text-center py-8">Loading notifications...</div>
      ) : filtered.length === 0 ? (
        <div className="glass-card p-12 text-center">
          <Bell className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <p className="text-slate-400">No notifications found</p>
        </div>
      ) : (
        <div className="space-y-3">
          {filtered.map(notif => {
            const Icon = TYPE_ICONS[notif.notificationType] || Info;
            const colorClass = TYPE_COLORS[notif.notificationType] || TYPE_COLORS.GENERAL;
            return (
              <div
                key={notif.id}
                onClick={() => !notif.read && markRead(notif.id)}
                className={`glass-card p-5 flex items-start gap-4 cursor-pointer hover:bg-slate-800/60 transition-all ${!notif.read ? 'border-indigo-500/30' : ''}`}
              >
                <div className={`p-2.5 rounded-xl border flex-shrink-0 ${colorClass}`}>
                  <Icon className="w-5 h-5" />
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <p className={`font-semibold text-sm ${!notif.read ? 'text-slate-100' : 'text-slate-300'}`}>
                        {notif.title || notif.notificationType?.replace('_', ' ')}
                      </p>
                      <p className="text-sm text-slate-400 mt-0.5">{notif.message}</p>
                    </div>
                    <div className="flex flex-col items-end gap-2 flex-shrink-0">
                      <span className="text-xs text-slate-500">
                        {notif.createdAt ? new Date(notif.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit' }) : ''}
                      </span>
                      {!notif.read && (
                        <span className="w-2.5 h-2.5 rounded-full bg-indigo-500 shadow-lg shadow-indigo-500/40" />
                      )}
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

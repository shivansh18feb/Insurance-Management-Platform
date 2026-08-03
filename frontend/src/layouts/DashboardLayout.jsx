import React, { useEffect, useState, useRef } from 'react';
import { Link, useNavigate, useLocation, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { searchApi, notificationApi } from '../services/api';
import {
  Shield, LayoutDashboard, Users, FileText, AlertCircle,
  CreditCard, Folder, BarChart3, LogOut, UserCheck, Search, Bell,
  UserCog, Briefcase, DollarSign, Settings, X, ChevronRight,
  TrendingUp, Wallet
} from 'lucide-react';

export const DashboardLayout = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState(null);
  const [searching, setSearching] = useState(false);
  const [unreadCount, setUnreadCount] = useState(0);
  const searchRef = useRef(null);

  const role = user?.role || 'CUSTOMER';

  const navGroups = [
    {
      title: 'Overview',
      items: [
        { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard, roles: ['ADMIN', 'AGENT', 'CUSTOMER', 'EMPLOYEE'] },
      ]
    },
    {
      title: 'Insurance',
      items: [
        { label: 'Customers', path: '/customers', icon: Users, roles: ['ADMIN', 'AGENT', 'EMPLOYEE'] },
        { label: 'Policies', path: '/policies', icon: FileText, roles: ['ADMIN', 'AGENT', 'CUSTOMER', 'EMPLOYEE'] },
        { label: 'Claims', path: '/claims', icon: AlertCircle, roles: ['ADMIN', 'AGENT', 'CUSTOMER', 'EMPLOYEE'] },
        { label: 'Premiums', path: '/premiums', icon: CreditCard, roles: ['ADMIN', 'AGENT', 'CUSTOMER', 'EMPLOYEE'] },
        { label: 'Payments', path: '/payments', icon: Wallet, roles: ['ADMIN', 'EMPLOYEE'] },
      ]
    },
    {
      title: 'Documents & Reports',
      items: [
        { label: 'Documents', path: '/documents', icon: Folder, roles: ['ADMIN', 'AGENT', 'CUSTOMER', 'EMPLOYEE'] },
        { label: 'Reports', path: '/reports', icon: BarChart3, roles: ['ADMIN'] },
      ]
    },
    {
      title: 'Administration',
      items: [
        { label: 'Agents', path: '/agents', icon: UserCheck, roles: ['ADMIN'] },
        { label: 'Employees', path: '/employees', icon: Briefcase, roles: ['ADMIN'] },
        { label: 'User Management', path: '/users', icon: UserCog, roles: ['ADMIN'] },
      ]
    },
  ];

  useEffect(() => {
    fetchUnreadCount();
    const interval = setInterval(fetchUnreadCount, 30000);
    return () => clearInterval(interval);
  }, []);

  const fetchUnreadCount = async () => {
    try {
      const res = await notificationApi.getUnreadCount();
      setUnreadCount(res.data.data || 0);
    } catch (e) {
      // silent fail
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;
    setSearching(true);
    try {
      const res = await searchApi.query(searchQuery);
      setSearchResults(res.data.data);
    } catch (err) {
      console.error(err);
    } finally {
      setSearching(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-slate-950 flex text-slate-100">
      {/* Sidebar */}
      <aside className="w-64 bg-slate-900 border-r border-slate-800 flex flex-col justify-between py-4 px-3 sticky top-0 h-screen overflow-y-auto">
        <div>
          {/* Logo */}
          <div className="flex items-center gap-3 px-3 py-3 mb-4">
            <div className="p-2.5 bg-gradient-to-tr from-indigo-600 to-indigo-500 rounded-xl shadow-lg shadow-indigo-500/30">
              <Shield className="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 className="font-bold text-lg leading-tight bg-gradient-to-r from-white to-slate-400 bg-clip-text text-transparent">InsurTech</h1>
              <span className="text-xs text-indigo-400 font-medium">Enterprise Platform</span>
            </div>
          </div>

          {/* Navigation Groups */}
          <nav className="space-y-4">
            {navGroups.map((group) => {
              const filtered = group.items.filter(item => item.roles.includes(role));
              if (filtered.length === 0) return null;
              return (
                <div key={group.title}>
                  <p className="px-3 text-[10px] font-bold uppercase tracking-widest text-slate-600 mb-1.5">{group.title}</p>
                  <div className="space-y-0.5">
                    {filtered.map((item) => {
                      const Icon = item.icon;
                      const isActive = location.pathname === item.path;
                      return (
                        <Link
                          key={item.path}
                          to={item.path}
                          className={`flex items-center gap-3 px-3.5 py-2.5 rounded-xl font-medium text-sm transition-all duration-200 ${
                            isActive
                              ? 'bg-indigo-600/20 text-indigo-400 border border-indigo-500/30 shadow-md shadow-indigo-500/10'
                              : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                          }`}
                        >
                          <Icon className={`w-4.5 h-4.5 ${isActive ? 'text-indigo-400' : 'text-slate-500'}`} style={{ width: '18px', height: '18px' }} />
                          {item.label}
                          {isActive && <ChevronRight className="w-3.5 h-3.5 ml-auto text-indigo-500/60" />}
                        </Link>
                      );
                    })}
                  </div>
                </div>
              );
            })}
          </nav>
        </div>

        {/* User Card & Bottom Actions */}
        <div className="pt-4 border-t border-slate-800/80 space-y-1 mt-4">
          <Link
            to="/notifications"
            className="flex items-center gap-3 px-3 py-2.5 text-sm text-slate-400 hover:text-slate-200 hover:bg-slate-800/60 rounded-xl transition-all"
          >
            <Bell className="w-4 h-4" />
            Notifications
            {unreadCount > 0 && (
              <span className="ml-auto bg-indigo-600 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full">
                {unreadCount > 99 ? '99+' : unreadCount}
              </span>
            )}
          </Link>
          <Link
            to="/settings"
            className="flex items-center gap-3 px-3 py-2.5 text-sm text-slate-400 hover:text-slate-200 hover:bg-slate-800/60 rounded-xl transition-all"
          >
            <Settings className="w-4 h-4" />
            Settings
          </Link>

          <div className="pt-3 border-t border-slate-800/80">
            <div className="flex items-center gap-3 px-2 py-1 mb-2">
              <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-indigo-500 to-indigo-700 flex items-center justify-center font-bold text-white shadow-md flex-shrink-0">
                {user?.email?.charAt(0).toUpperCase() || 'U'}
              </div>
              <div className="flex-1 truncate min-w-0">
                <p className="text-sm font-semibold text-slate-200 truncate">{user?.email}</p>
                <span className="text-[10px] uppercase font-bold tracking-wider px-2 py-0.5 rounded-full bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
                  {role}
                </span>
              </div>
            </div>
            <button
              onClick={handleLogout}
              className="w-full flex items-center gap-2.5 px-3 py-2 text-sm font-medium text-rose-400 hover:bg-rose-500/10 rounded-xl transition-all cursor-pointer"
            >
              <LogOut className="w-4 h-4" />
              Sign Out
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="flex-1 flex flex-col min-w-0">
        {/* Top Header */}
        <header className="h-16 bg-slate-900/60 backdrop-blur-md border-b border-slate-800 px-8 flex items-center justify-between sticky top-0 z-10">
          {/* Search Bar */}
          <form onSubmit={handleSearch} className="relative">
            <div className="flex items-center gap-3 bg-slate-950/60 border border-slate-800 rounded-xl px-3.5 py-1.5 w-80 focus-within:border-indigo-500/60 transition-all">
              <Search className="w-4 h-4 text-slate-400 flex-shrink-0" />
              <input
                ref={searchRef}
                type="text"
                value={searchQuery}
                onChange={(e) => { setSearchQuery(e.target.value); if (!e.target.value) setSearchResults(null); }}
                placeholder="Search policies, customers, claims..."
                className="bg-transparent text-sm text-slate-200 placeholder-slate-500 focus:outline-none w-full"
              />
              {searchQuery && (
                <button type="button" onClick={() => { setSearchQuery(''); setSearchResults(null); }} className="text-slate-500 hover:text-slate-300">
                  <X className="w-3.5 h-3.5" />
                </button>
              )}
            </div>
            {/* Search Dropdown */}
            {searchResults !== null && (
              <div className="absolute top-full mt-2 w-96 glass-card p-4 z-50 space-y-4 shadow-2xl shadow-black/60">
                {Object.keys(searchResults).length === 0 ? (
                  <p className="text-sm text-slate-400 text-center py-2">No results found</p>
                ) : (
                  Object.entries(searchResults).map(([category, items]) =>
                    items.length > 0 ? (
                      <div key={category}>
                        <p className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">{category}</p>
                        {items.slice(0, 3).map((item, i) => (
                          <div key={i} className="flex items-center gap-2 py-1.5 px-2 hover:bg-slate-800/60 rounded-lg cursor-pointer text-sm text-slate-300">
                            <ChevronRight className="w-3.5 h-3.5 text-indigo-400" />
                            {item.name || item.policyNumber || item.claimId || JSON.stringify(item).slice(0, 40)}
                          </div>
                        ))}
                      </div>
                    ) : null
                  )
                )}
              </div>
            )}
          </form>

          <div className="flex items-center gap-3">
            <Link
              to="/notifications"
              className="p-2 text-slate-400 hover:text-slate-200 bg-slate-800/60 rounded-xl border border-slate-700/60 hover:bg-slate-800 relative cursor-pointer transition-all"
            >
              <Bell className="w-5 h-5" />
              {unreadCount > 0 && (
                <span className="absolute -top-1 -right-1 w-4 h-4 bg-indigo-600 text-white text-[9px] font-bold rounded-full flex items-center justify-center ring-2 ring-slate-950">
                  {unreadCount > 9 ? '9+' : unreadCount}
                </span>
              )}
            </Link>
            <Link
              to="/settings"
              className="p-2 text-slate-400 hover:text-slate-200 bg-slate-800/60 rounded-xl border border-slate-700/60 hover:bg-slate-800 cursor-pointer transition-all"
            >
              <Settings className="w-5 h-5" />
            </Link>
          </div>
        </header>

        {/* Page Content Outlet */}
        <div className="p-8 flex-1">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

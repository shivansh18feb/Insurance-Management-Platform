import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { adminApi, policyApi, claimApi, customerApi } from '../services/api';
import { 
  Users, FileText, AlertCircle, IndianRupee, TrendingUp, ShieldCheck, 
  PlusCircle, Award, CreditCard, CheckCircle2, Clock, ArrowRight, Shield 
} from 'lucide-react';
import { Link } from 'react-router-dom';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Bar, Pie } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

export const AdminDashboardPage = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const role = user?.role || 'CUSTOMER';

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const res = await adminApi.getDashboard();
      setStats(res.data.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-slate-400 text-center py-12">Loading Dashboard...</div>;
  }

  // Define role-specific header titles and KPIs
  const getRoleHeader = () => {
    switch (role) {
      case 'ADMIN':
        return {
          title: 'Enterprise Executive Dashboard',
          subtitle: 'Real-time insurance operational overview, revenue analytics, and system metrics',
        };
      case 'AGENT':
        return {
          title: 'Insurance Agent Portal',
          subtitle: 'Manage client portfolio, track sales performance, and view commission earned',
        };
      case 'EMPLOYEE':
        return {
          title: 'Staff Operations Workspace',
          subtitle: 'Process policy applications, review claim submissions, and verify uploaded documents',
        };
      case 'CUSTOMER':
      default:
        return {
          title: 'Customer Self-Service Hub',
          subtitle: 'Overview of your active policy coverage, claims status, and premium schedules',
        };
    }
  };

  const getKPIs = () => {
    switch (role) {
      case 'ADMIN':
        return [
          { title: 'Total Customers', value: stats?.totalCustomers || 0, icon: Users, color: 'from-blue-500 to-indigo-600' },
          { title: 'Active Policies', value: stats?.activePolicies || 0, icon: FileText, color: 'from-emerald-500 to-teal-600' },
          { title: 'Pending Claims', value: stats?.pendingClaims || 0, icon: AlertCircle, color: 'from-amber-500 to-orange-600' },
          { title: 'Total Revenue', value: `₹${(stats?.totalRevenue || 0).toLocaleString('en-IN')}`, icon: IndianRupee, color: 'from-purple-500 to-pink-600' },
        ];
      case 'AGENT':
        return [
          { title: 'Assigned Clients', value: stats?.totalCustomers || 0, icon: Users, color: 'from-blue-500 to-indigo-600' },
          { title: 'Active Policies', value: stats?.activePolicies || 0, icon: FileText, color: 'from-emerald-500 to-teal-600' },
          { title: 'Pending Claims', value: stats?.pendingClaims || 0, icon: Clock, color: 'from-amber-500 to-orange-600' },
          { title: 'Est. Commission', value: `₹${((stats?.totalRevenue || 0) * 0.05).toLocaleString('en-IN')}`, icon: Award, color: 'from-purple-500 to-pink-600' },
        ];
      case 'EMPLOYEE':
        return [
          { title: 'Total Policies', value: stats?.totalPolicies || 0, icon: FileText, color: 'from-blue-500 to-indigo-600' },
          { title: 'Active Policies', value: stats?.activePolicies || 0, icon: CheckCircle2, color: 'from-emerald-500 to-teal-600' },
          { title: 'Claims Under Review', value: stats?.pendingClaims || 0, icon: AlertCircle, color: 'from-amber-500 to-orange-600' },
          { title: 'Settled Claims', value: stats?.settledClaims || 0, icon: ShieldCheck, color: 'from-purple-500 to-pink-600' },
        ];
      case 'CUSTOMER':
      default:
        return [
          { title: 'My Active Policies', value: stats?.activePolicies || 0, icon: Shield, color: 'from-emerald-500 to-teal-600' },
          { title: 'Total Claims Filed', value: stats?.totalClaims || 0, icon: FileText, color: 'from-blue-500 to-indigo-600' },
          { title: 'Pending Claims', value: stats?.pendingClaims || 0, icon: Clock, color: 'from-amber-500 to-orange-600' },
          { title: 'Total Payout Received', value: `₹${(stats?.totalClaimPayout || 0).toLocaleString('en-IN')}`, icon: IndianRupee, color: 'from-purple-500 to-pink-600' },
        ];
    }
  };

  const header = getRoleHeader();
  const kpis = getKPIs();

  const pieData = {
    labels: stats?.policiesByType ? Object.keys(stats.policiesByType) : [],
    datasets: [{
      data: stats?.policiesByType ? Object.values(stats.policiesByType) : [],
      backgroundColor: ['#6366f1', '#10b981', '#f59e0b', '#ec4899', '#8b5cf6'],
      borderWidth: 0,
    }]
  };

  const barData = {
    labels: stats?.claimsByStatus ? Object.keys(stats.claimsByStatus) : [],
    datasets: [{
      label: 'Claims Count',
      data: stats?.claimsByStatus ? Object.values(stats.claimsByStatus) : [],
      backgroundColor: '#6366f1',
      borderRadius: 8,
    }]
  };

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">{header.title}</h1>
          <p className="text-sm text-slate-400">{header.subtitle}</p>
        </div>
        <span className="text-xs uppercase font-bold tracking-widest px-3 py-1.5 rounded-full bg-indigo-500/20 text-indigo-400 border border-indigo-500/30">
          Role: {role}
        </span>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {kpis.map((kpi, i) => {
          const Icon = kpi.icon;
          return (
            <div key={i} className="glass-card p-6 relative overflow-hidden flex items-center justify-between hover:border-slate-700 transition-all">
              <div>
                <p className="text-xs font-semibold uppercase tracking-wider text-slate-400">{kpi.title}</p>
                <h3 className="text-2xl font-bold text-slate-100 mt-2">{kpi.value}</h3>
              </div>
              <div className={`p-3.5 rounded-2xl bg-gradient-to-tr ${kpi.color} shadow-lg text-white`}>
                <Icon className="w-6 h-6" />
              </div>
            </div>
          );
        })}
      </div>

      {/* Quick Action Bar for Roles */}
      <div className="glass-card p-6 border-l-4 border-indigo-500">
        <h3 className="text-sm font-bold uppercase tracking-wider text-slate-400 mb-4">Quick Operational Actions</h3>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {role === 'ADMIN' && (
            <>
              <Link to="/customers" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Add Customer</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/agents" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Onboard Agent</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/reports" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Export PDF/Excel</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/users" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Manage Roles</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
            </>
          )}
          {role === 'AGENT' && (
            <>
              <Link to="/customers" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Register Client</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/policies" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Issue Policy</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/documents" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Upload KYC</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/claims" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Track Client Claims</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
            </>
          )}
          {role === 'EMPLOYEE' && (
            <>
              <Link to="/claims" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Review Claims</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/documents" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Verify Documents</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/payments" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Record Payments</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/customers" className="btn-secondary flex items-center justify-between group text-xs">
                <span>View Customers</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
            </>
          )}
          {role === 'CUSTOMER' && (
            <>
              <Link to="/policies" className="btn-secondary flex items-center justify-between group text-xs">
                <span>View My Policies</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/claims" className="btn-secondary flex items-center justify-between group text-xs">
                <span>File a Claim</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/premiums" className="btn-secondary flex items-center justify-between group text-xs">
                <span>Pay Premium</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link to="/documents" className="btn-secondary flex items-center justify-between group text-xs">
                <span>My Documents</span>
                <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
              </Link>
            </>
          )}
        </div>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="glass-card p-6">
          <h3 className="text-base font-semibold text-slate-200 mb-6 flex items-center gap-2">
            <TrendingUp className="w-5 h-5 text-indigo-400" />
            Policies Distribution by Type
          </h3>
          <div className="h-64 flex justify-center">
            <Pie data={pieData} options={{ maintainAspectRatio: false }} />
          </div>
        </div>

        <div className="glass-card p-6">
          <h3 className="text-base font-semibold text-slate-200 mb-6 flex items-center gap-2">
            <ShieldCheck className="w-5 h-5 text-indigo-400" />
            Claims Status Analytics
          </h3>
          <div className="h-64">
            <Bar data={barData} options={{ maintainAspectRatio: false, responsive: true }} />
          </div>
        </div>
      </div>
    </div>
  );
};

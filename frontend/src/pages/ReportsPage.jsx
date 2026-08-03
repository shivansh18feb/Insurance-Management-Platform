import React from 'react';
import { reportApi } from '../services/api';
import { FileText, Download, Table, BarChart2 } from 'lucide-react';

export const ReportsPage = () => {
  return (
    <div className="space-y-6 max-w-4xl">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Business Reports & Export</h1>
        <p className="text-sm text-slate-400">Generate executive PDF reports and Excel analytics exports</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="glass-card p-6 flex flex-col justify-between">
          <div>
            <div className="p-3 bg-indigo-500/20 text-indigo-400 rounded-xl w-fit mb-4 border border-indigo-500/30">
              <FileText className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-bold text-slate-100">Policy Summary PDF</h3>
            <p className="text-xs text-slate-400 mt-2">Export comprehensive PDF report of active & total insurance policies.</p>
          </div>
          <a 
            href={reportApi.policyPdfUrl} 
            target="_blank" 
            rel="noopener noreferrer"
            className="btn-primary mt-6 flex items-center justify-center gap-2"
          >
            <Download className="w-4 h-4" />
            Download PDF
          </a>
        </div>

        <div className="glass-card p-6 flex flex-col justify-between">
          <div>
            <div className="p-3 bg-emerald-500/20 text-emerald-400 rounded-xl w-fit mb-4 border border-emerald-500/30">
              <BarChart2 className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-bold text-slate-100">Claim Settlement PDF</h3>
            <p className="text-xs text-slate-400 mt-2">Export detailed claim settlement breakdown report in formatted PDF.</p>
          </div>
          <a 
            href={reportApi.claimPdfUrl} 
            target="_blank" 
            rel="noopener noreferrer"
            className="btn-primary mt-6 flex items-center justify-center gap-2"
          >
            <Download className="w-4 h-4" />
            Download PDF
          </a>
        </div>

        <div className="glass-card p-6 flex flex-col justify-between">
          <div>
            <div className="p-3 bg-amber-500/20 text-amber-400 rounded-xl w-fit mb-4 border border-amber-500/30">
              <Table className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-bold text-slate-100">Business Overview Excel</h3>
            <p className="text-xs text-slate-400 mt-2">Download raw spreadsheet `.xlsx` data file for offline Excel analysis.</p>
          </div>
          <a 
            href={reportApi.excelUrl} 
            download
            className="btn-primary mt-6 flex items-center justify-center gap-2"
          >
            <Download className="w-4 h-4" />
            Export Excel
          </a>
        </div>
      </div>
    </div>
  );
};

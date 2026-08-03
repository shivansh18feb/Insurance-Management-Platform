import React, { useState } from 'react';
import { documentApi } from '../services/api';
import { Upload, Download, CheckCircle, FileText } from 'lucide-react';

export const DocumentsPage = () => {
  const [file, setFile] = useState(null);
  const [customerId, setCustomerId] = useState('');
  const [documentType, setDocumentType] = useState('IDENTITY');
  const [description, setDescription] = useState('');
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState('');

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!file || !customerId) {
      alert('Please select a file and enter Customer ID');
      return;
    }
    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);
    formData.append('customerId', customerId);
    formData.append('documentType', documentType);
    formData.append('description', description);

    try {
      await documentApi.upload(formData);
      setMessage('Document uploaded successfully!');
      setFile(null);
    } catch (err) {
      alert(err.response?.data?.message || 'Upload failed');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="space-y-6 max-w-3xl">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Document Management</h1>
        <p className="text-sm text-slate-400">Upload identity verification, policy & claim documents</p>
      </div>

      <div className="glass-card p-6 space-y-4">
        <h3 className="text-base font-semibold text-slate-200">Upload New File</h3>

        {message && (
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 rounded-xl text-sm flex items-center gap-2">
            <CheckCircle className="w-4 h-4" />
            {message}
          </div>
        )}

        <form onSubmit={handleUpload} className="space-y-4">
          <input 
            type="number" 
            placeholder="Customer ID" 
            required 
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value)}
            className="glass-input w-full"
          />

          <select 
            value={documentType} 
            onChange={(e) => setDocumentType(e.target.value)}
            className="glass-input w-full"
          >
            <option value="IDENTITY">Identity Verification (Aadhaar/PAN/Passport)</option>
            <option value="POLICY">Policy Document</option>
            <option value="CLAIM">Claim Supporting Document</option>
            <option value="MEDICAL">Medical Records</option>
            <option value="OTHER">Other File</option>
          </select>

          <input 
            type="text" 
            placeholder="File Description (optional)" 
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="glass-input w-full"
          />

          <div className="border-2 border-dashed border-slate-700/80 hover:border-indigo-500/60 rounded-2xl p-6 text-center transition-colors">
            <input 
              type="file" 
              required 
              onChange={(e) => setFile(e.target.files[0])}
              className="hidden"
              id="file-upload"
            />
            <label htmlFor="file-upload" className="cursor-pointer flex flex-col items-center gap-2">
              <Upload className="w-8 h-8 text-indigo-400" />
              <span className="text-sm text-slate-300 font-medium">
                {file ? file.name : 'Click to select or drag and drop file'}
              </span>
              <span className="text-xs text-slate-500">PDF, JPG, PNG up to 10MB</span>
            </label>
          </div>

          <button type="submit" disabled={uploading} className="btn-primary w-full flex items-center justify-center gap-2">
            <Upload className="w-4 h-4" />
            {uploading ? 'Uploading File...' : 'Upload Document'}
          </button>
        </form>
      </div>
    </div>
  );
};

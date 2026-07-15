import React from 'react';
import { UploadCloud, File, X, CheckCircle } from 'lucide-react';

interface LabResultUploaderProps {
  onFileChange: (file: File | null) => void;
  selectedFile: File | null;
  uploadedUrl?: string | null;
}

export const LabResultUploader: React.FC<LabResultUploaderProps> = ({ onFileChange, selectedFile, uploadedUrl }) => {
  const fileInputRef = React.useRef<HTMLInputElement>(null);

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      const file = e.dataTransfer.files[0];
      if (file.type === 'application/pdf') {
        onFileChange(file);
      } else {
        alert('Please drop a valid PDF file.');
      }
    }
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      onFileChange(e.target.files[0]);
    }
  };

  const handleRemoveFile = () => {
    onFileChange(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="space-y-4">
      <div 
        onDragOver={handleDragOver}
        onDrop={handleDrop}
        onClick={() => fileInputRef.current?.click()}
        className="border-2 border-dashed border-slate-300 hover:border-indigo-400 bg-slate-50 hover:bg-indigo-50/20 rounded-xl p-6 text-center cursor-pointer transition flex flex-col items-center justify-center gap-2 group"
      >
        <input 
          type="file" 
          ref={fileInputRef} 
          onChange={handleFileSelect} 
          accept="application/pdf"
          className="hidden" 
        />
        
        <UploadCloud className="h-10 w-10 text-slate-400 group-hover:text-indigo-600 transition" />
        <div>
          <p className="text-sm font-bold text-slate-700">Drag & drop lab result PDF here</p>
          <p className="text-xs text-slate-400 mt-1">or click to browse from files</p>
        </div>
      </div>

      {/* File Preview */}
      {selectedFile && (
        <div className="flex items-center justify-between p-3.5 bg-indigo-50/50 border border-indigo-100 rounded-xl animate-fade-in">
          <div className="flex items-center space-x-3 truncate">
            <div className="bg-indigo-100 p-2 rounded-lg text-indigo-600">
              <File className="h-5 w-5" />
            </div>
            <div className="truncate">
              <p className="text-sm font-bold text-slate-750 truncate">{selectedFile.name}</p>
              <p className="text-xs text-slate-400">{(selectedFile.size / 1024).toFixed(1)} KB</p>
            </div>
          </div>
          <button 
            type="button" 
            onClick={(e) => { e.stopPropagation(); handleRemoveFile(); }}
            className="p-1 rounded-full hover:bg-slate-200 text-slate-400 hover:text-slate-600 transition"
          >
            <X className="h-4 w-4" />
          </button>
        </div>
      )}

      {/* Uploaded Url Confirmation */}
      {uploadedUrl && !selectedFile && (
        <div className="flex items-center space-x-2 text-xs font-semibold text-green-700 bg-green-50 p-3.5 border border-green-200 rounded-xl">
          <CheckCircle className="h-4 w-4 flex-shrink-0" />
          <span className="truncate">Active result: <a href={uploadedUrl} target="_blank" rel="noreferrer" className="underline hover:text-green-900">{uploadedUrl.split('/').pop()}</a></span>
        </div>
      )}
    </div>
  );
};
export default LabResultUploader;

import React from 'react';

interface ZaloWidgetProps {
  /**
   * Zalo phone number or official account link.
   * If it's a phone number, it should be in standard Vietnamese format (e.g., '0912345678').
   * Default is '0912345678'.
   */
  phoneNumber?: string;
  /**
   * Custom tooltip message to display on hover.
   * Default is 'Liên hệ Zalo hỗ trợ'
   */
  tooltipText?: string;
}

export const ZaloWidget: React.FC<ZaloWidgetProps> = ({
  phoneNumber = '0912345678', // Thay thế số điện thoại Zalo của phòng khám tại đây
  tooltipText = 'Liên hệ Zalo hỗ trợ'
}) => {
  // Format the link: if it doesn't start with http, assume it's a phone number and build the zalo.me link
  const zaloUrl = phoneNumber.startsWith('http') 
    ? phoneNumber 
    : `https://zalo.me/${phoneNumber.replace(/\s+/g, '')}`;

  return (
    <div className="fixed bottom-6 right-6 z-50 flex items-center group">
      {/* Tooltip Label */}
      <div className="mr-3 bg-white text-slate-800 text-sm font-semibold px-4 py-2 rounded-xl shadow-lg border border-slate-100 opacity-0 transform translate-x-4 pointer-events-none group-hover:opacity-100 group-hover:translate-x-0 transition-all duration-300 whitespace-nowrap flex items-center space-x-2">
        <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
        <span>{tooltipText}</span>
      </div>

      {/* Zalo Button Link */}
      <a
        href={zaloUrl}
        target="_blank"
        rel="noopener noreferrer"
        title="Liên hệ qua Zalo"
        className="relative flex items-center justify-center w-14 h-14 rounded-full bg-[#0068FF] text-white shadow-[0_4px_20px_rgba(0,104,255,0.45)] hover:shadow-[0_8px_30px_rgba(0,104,255,0.6)] hover:scale-110 active:scale-95 transition-all duration-300 cursor-pointer overflow-visible"
      >
        {/* Pulsating Glow Ring */}
        <span className="absolute inset-0 rounded-full bg-[#0068FF] opacity-40 animate-ping pointer-events-none"></span>

        {/* Online Status Dot */}
        <span className="absolute top-0 right-0 w-3.5 h-3.5 bg-emerald-500 border-2 border-white rounded-full z-10"></span>

        {/* Custom Zalo Icon SVG */}
        <svg
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className="w-8 h-8 drop-shadow-sm transition-transform duration-300 group-hover:rotate-[10deg]"
        >
          {/* Outer Speech Bubble in White */}
          <path
            d="M12 4C7.03 4 3 7.58 3 12C3 13.9 3.75 15.65 5 17.03L4.5 19.5L7.25 18.6C8.6 19.5 10.25 20 12 20C16.97 20 21 16.42 21 12C21 7.58 16.97 4 12 4Z"
            fill="white"
          />
          {/* Bold Styled letter "Z" in Zalo's Signature Blue */}
          <path
            d="M9.5 9.5H14.5L9.5 14.5H14.5"
            stroke="#0068FF"
            strokeWidth="2.2"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </a>
    </div>
  );
};

export default ZaloWidget;

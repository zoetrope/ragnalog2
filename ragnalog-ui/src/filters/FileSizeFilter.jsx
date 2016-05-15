export default function FileSizeFilter(data, digits) {
  const bytes = parseFloat(data);
  const units = ['B', 'kB', 'MB', 'GB', 'TB', 'PB'];
  const number = Math.floor(Math.log(bytes) / Math.log(1024));
  return (bytes / Math.pow(1024, Math.floor(number))).toFixed(parseInt(digits)) + ' ' + units[number];
};

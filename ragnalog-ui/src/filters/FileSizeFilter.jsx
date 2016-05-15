export default function FileSizeFilter(input, digits) {
  if (input !== null && input !== "") {
    const bytes = parseFloat(input);
    const units = ['B', 'kB', 'MB', 'GB', 'TB', 'PB'];
    const number = Math.floor(Math.log(bytes) / Math.log(1024));
    return (bytes / Math.pow(1024, Math.floor(number))).toFixed(parseInt(digits)) + ' ' + units[number];
  } else {
    return '-';
  }
};

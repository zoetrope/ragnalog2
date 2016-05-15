import moment from "moment";

moment.locale("ja");

export default function DateTimeFilter(input, format) {
  if (input !== null && input !== "") {
    return moment(input).format(format);
  } else {
    return '-';
  }
}


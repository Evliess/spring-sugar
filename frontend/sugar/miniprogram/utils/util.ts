const b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
const btoa = (value: String) => {
  value = String(value);
    var bitmap, a, b, c, result = "", i = 0, rest = value.length % 3;
    for (; i < value.length;) {
        if ((a = value.charCodeAt(i++)) > 255 ||
            (b = value.charCodeAt(i++)) > 255 ||
            (c = value.charCodeAt(i++)) > 255)
            throw new TypeError("Failed to execute 'btoa' on 'Window': The string to be encoded contains characters outside of the Latin1 range.");
        bitmap = (a << 16) | (b << 8) | c;
        result += b64.charAt(bitmap >> 18 & 63) + b64.charAt(bitmap >> 12 & 63) +
            b64.charAt(bitmap >> 6 & 63) + b64.charAt(bitmap & 63);
    }
    return rest ? result.slice(0, rest - 3) + "===".substring(rest) : result;
}

module.exports = {
  btoa
}



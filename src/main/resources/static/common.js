// 公共 Ajax 工具函数
function ajax_get(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            try { callback(JSON.parse(xhr.responseText)); }
            catch(e) { callback({code:500, msg:'请求失败'}); }
        }
    };
    xhr.send(null);
}

function ajax_post(url, data, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            try { callback(JSON.parse(xhr.responseText)); }
            catch(e) { callback({code:500, msg:'请求失败'}); }
        }
    };
    xhr.send(data ? JSON.stringify(data) : null);
}

// 检查登录状态
function checkLogin() {
    var u = localStorage.getItem('loginUser');
    if (!u) { window.location.href = '/login.html'; return null; }
    return JSON.parse(u);
}

// 格式化金额
function fmtMoney(amount) {
    if (amount === null || amount === undefined) return '0.00';
    return parseFloat(amount).toFixed(2);
}

// 格式化时间
function fmtTime(timeStr) {
    if (!timeStr) return '-';
    return timeStr.replace('T', ' ').substring(0, 19);
}

// 平滑页面跳转（无纯色遮盖，淡出 → 淡入）
function navigateTo(url) {
    document.body.style.transition = 'opacity 0.2s ease';
    document.body.style.opacity = '0';
    setTimeout(function() { window.location.href = url; }, 220);
}

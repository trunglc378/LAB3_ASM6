function dupilicateQuestions() {
    // Kiểm tra trùng lặp câu hỏi
    var question1 = document.getElementById("hintQuestion1").value;
    var question2 = document.getElementById("hintQuestion2").value;
    var question3 = document.getElementById("hintQuestion3").value;
    if (question1 === question2 || question2 === question3 || question1 === question3) {
        alert("Vui lòng chọn các câu hỏi khác nhau");
        return false;
    }
}
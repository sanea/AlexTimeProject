function addTaskDialogSaveClick(args) {
    if(args && !args.validationFailed)
        addTaskDlg.hide();
}

function executeTaskTimeDialogClick(args) {
    if(args && !args.validationFailed)
        timeDialog.hide();
}
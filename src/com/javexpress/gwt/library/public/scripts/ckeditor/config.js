CKEDITOR.editorConfig = function( config ){
    config.toolbar = 'jestudio';
    config.toolbar_jestudio = [
     ['Bold','Italic','Underline','Strike','-','Subscript','Superscript','-', 'NumberedList','BulletedList','-','Outdent','Indent'],
     ['Cut','Copy','Paste','PasteText','PasteFromWord'],
     ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
     ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
     ['Maximize', 'ShowBlocks', 'Table']
    ];
};
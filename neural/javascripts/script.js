
$(document).ready(function(){
    $('#facial').hide();
    $('#char').click(function(){
        $('#facial').hide();
        $('#character').fadeIn('slow');
    });
    $('#face').click(function(){
        $('#character').hide();
        $('#facial').fadeIn('slow');
    });
});

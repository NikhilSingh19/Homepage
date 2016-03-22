
$(document).ready(function(){
    $('#num').hide();
    $('#quote').hide();
    $('#players').click(function(){
        $('#num').hide();
        $('#quote').hide();
        $('#hockey').fadeIn('slow');
    });
    $('#stock').click(function(){
        $('#hockey').hide();
        $('#num').hide();
        $('#quote').fadeIn('slow');
    });
    $('#guess').click(function(){
        $('#quote').hide();
        $('#hockey').hide();
        $('#num').fadeIn('slow');
    });
});

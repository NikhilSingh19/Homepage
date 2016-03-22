
$(document).ready(function(){
    $('#hockey2').hide();
    $('#hs').click(function(){
        $('#hockey2').hide();
        $('#hockey').fadeIn('slow');
    });
    $('#hs2').click(function(){
        $('#hockey').hide();
        $('#hockey2').fadeIn('slow');
    });
});

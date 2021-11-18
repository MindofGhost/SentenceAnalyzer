const API = "API.php";
var resultNUM;
var lasthash = "";
var pause= false;
var links= {"#text":"/text.htm","#result":"/result.htm","#404":"/404.htm"};
function load(hash){
	if (hash.startsWith('#result/')) hash="#result";
	if (links[hash]==undefined) hash="#404"
	else $('a[href$="'+hash+'"]:first').parent().addClass('active')
	$.post(links[hash])
	.done(function(data) {
		$("#page").empty();
		$("#page").append(data);
	})
	.fail(function(xhr) {
		$("#page").empty();
		$("#page").append(xhr.responseText);
		$('#loader').hide();
		$('#page').fadeIn('slow');
	});
}
$(document).ready(function(){
	$(window).trigger('hashchange');
	//обрабатываем перемещения по страницам
    $(window).on('hashchange', function(e){
		$('#page').fadeOut('slow');
		$('#page').hide();
		$('#loader').show();
		$('#navbarNavDropdown .navbar-nav .nav-item').removeClass('active');
		console.log('hash change');
		if (window.location.hash == '') window.location.hash = '#text'
		//$.ajaxSetup({async: false});		
		load(window.location.hash);
		//$.ajaxSetup({async: true});
    });
	//тригерим проверку текущей страницы
	$(window).trigger('hashchange');
	
});
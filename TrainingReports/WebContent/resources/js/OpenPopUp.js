var w = 480, h = 340;
    
        if (document.all || document.layers) {
           w = screen.availWidth;
           h = screen.availHeight;
        }
        
        var popW = 660, popH = 300;
        
        var leftPos = (w-popW)/2, topPos = (h-popH)/2;
    
function openPopUp(url){
    window.open(url,'_new_temp','height=' + popH + ',width=' + popW + ',top=' + topPos + ',left=' + leftPos+',scrollbars=yes,location=no,status=yes,resizable = yes')
}
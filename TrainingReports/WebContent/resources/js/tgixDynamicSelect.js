



/****************************************************************************************

 *

 * This stores the options and values for a select list.

 *

 * param:

 *			name - this is the select form element ID

 ****************************************************************************************/	

function tgixSelect( name ) {

	// This is the ID of the select form element

	this.name = name;

	

	// list of options for this select

	this.options = new Array();



	// adds one option to the list.

	this.addOption = function(name,value,selected) {

		this.options[this.options.length] = new tgixOption( name, value, selected );

		return this.options[this.options.length-1];

	}



	// returns a single option object with the vaule as the key.

	this.getOption = function( value ) {

		for( var x = 0; x<this.options.length; x++ ) {

			if ( this.options[x].option.value == value ) {

				return this.options[x];

			}

		}

	}

}



/****************************************************************************************

 *

 * This stores one option object for a select.

 * 

 * param:

 *			name  - name that shows up in the drop down

 *			value - value of the item 

 ****************************************************************************************/	

function tgixOption( name, value, selected ) {

	// name of the option

	this.name = name;

	this.selected = selected;

	

	// stores the form Option object

	this.option = new Option( name, value );

	

	// if this option element has a dependant select list, that select object will be stored

	// select object will be stored here.

	this.child;

}

	

/****************************************************************************************

 *

 * This will initially populate all the select list that it is passed.

 *

 * param:

 *			data - this is a tgixSelect object.

 * 

 ****************************************************************************************/	

function populateTgixSelect( data ) {

	// gets the select object

	var tmpSel = document.getElementById( data.name );

	

	var tempData = data.options;

	

	

	while( tmpSel.options.length ) tmpSel.remove( 0 );



	for( var x=0; x<tempData.length; x++ ) {

		try	{

			tmpSel.add( tempData[x].option );

		} catch(e)	{

			tmpSel.add( tempData[x].option, null );

		}

	}

	tmpSel.selectedIndex=0;



	var key = tmpSel.options[tmpSel.selectedIndex].value;

	

	if (data.getOption( key ) != null) {

		if (data.getOption( key ).child != null) {

			populateTgixSelect( data.getOption(key).child );

		}

	}

}

	

	

	

/****************************************************************************************

 *

 * param:

 *			data - this is a tgixSelect object.

 *			source - calling form element 

 ****************************************************************************************/	

function updateTgixSelect( source, data ) {

	if ( data.name != source.name ) {

		var tmpSel = document.getElementById( data.name );

		var key = tmpSel.options[tmpSel.selectedIndex].value;

		if (data.getOption( key ) != null) {

			if (data.getOption( key ).child != null) {

				updateTgixSelect( source, data.getOption( key ).child );

			}

		}

	} else {

		var tmpSel = document.getElementById( data.name );

		var key = tmpSel.options[tmpSel.selectedIndex].value;

		if (data.getOption( key ) != null) {

			if (data.getOption( key ).child != null) {

				populateTgixSelect(data.getOption( key ).child);

			}

		}

	}

}
/****************************************************************************************
 *  
 *	data - this is a Select object.
 *  source - calling form element 
 ****************************************************************************************/	
function updateSalesOrg( source, data ) {

		var tmpBU = document.getElementById( data.name );
        var tmpSales = document.getElementById( "salesOrg" );
        //var tmpRole = document.getElementById( "role" );
        
        var tempTextbox = document.getElementById("selectedBU");
        tempTextbox.value = "";
        var flag = 1;
        
        while( tmpSales.options.length ) tmpSales.remove( 0 );
        //while( tmpRole.options.length ) tmpRole.remove( 0 );   
        addOption(tmpSales," All"," All");
        //addOption(tmpRole," All"," All");
        
        while ( tmpBU.options.selectedIndex >= 0 ) 
        {
            if (flag != 1)
                tempTextbox.value = tempTextbox.value + ";";
            var key = tmpBU.options[tmpBU.selectedIndex].value;
            if (data.getOption( key ) != null) 
            {
                if (data.getOption( key ).child != null) 
                {   
                    var verytempData = data.getOption( key ).child;
                    var tempData = verytempData.options;
                    
                    for( var x=0; x<tempData.length; x++ ) {
                        try	{
                            if(tempData[x].option.text != ' All' )
                                tmpSales.add( tempData[x].option );
                        } catch(e)	{
                            tmpSales.add( tempData[x].option, null );
                        }
                    }
                }
            }
            var toBeAddedAgain = tmpBU.options[tmpBU.selectedIndex].value;
            tmpBU.remove(tmpBU.options.selectedIndex); //Remove the item from Source Listbox    
            addOption(tmpBU,toBeAddedAgain,toBeAddedAgain);
    
            if(toBeAddedAgain != ' All')
            {
                tempTextbox.value = tempTextbox.value + toBeAddedAgain;
                flag = flag + 1;
            }
            /*else if (toBeAddedAgain == ' All')
                flag = 1;*/
            else if (toBeAddedAgain == ' All')
            {
            tempTextbox.value = tempTextbox.value + toBeAddedAgain;
            flag = 1;
            break;
            }

        }
        sortSelect(tmpBU);
        sortSelect(tmpSales);
}

/****************************************************************************************
 *  Method for populating the Role on selection of SalesOrg for Group Administration
 *	data - this is the Select object.
 *	source - calling form element 
 ****************************************************************************************/	
function updateRole( source, data ) {

        var tmpBU = document.getElementById( data.name );
        var tmpSales = document.getElementById( "salesOrg" );
        var tmpRole = document.getElementById( "role" );
        
        var tempTextbox = document.getElementById("selectedSalesorg");
        tempTextbox.value = "";
        var flag = 1;
        
        if(tmpSales.options[tmpSales.selectedIndex].value!= ' All')
        {
            while( tmpRole.options.length ) tmpRole.remove( 0 );  
            addOption(tmpRole," All"," All");
        }
        
        while ( tmpSales.options.selectedIndex >= 0 ) 
        {
            if (flag != 1)
                tempTextbox.value = tempTextbox.value + ";";
            var keySales = tmpSales.options[tmpSales.selectedIndex].value;
            var intlength = tmpBU.options.length;

            for(var j=0;j<intlength;j++) 
            {
                var keyBu = tmpBU.options[j].value;
                if (data.getOption( keyBu ) != null) 
                { 
                    if (data.getOption( keyBu ).child != null) 
                    {  
                        var buChilds = data.getOption( keyBu ).child;
                        var salesList = buChilds.options;
                    
                        for( var z=0; z<salesList.length; z++ ) 
                        {
                            if(salesList[z].option.text == keySales) 
                            {
                                if (buChilds.getOption( keySales ) != null) 
                                { 
                                    if (buChilds.getOption( keySales ).child != null) 
                                    {  
                                        var verytempData = buChilds.getOption( keySales ).child;
                                        var tempData = verytempData.options;
                                        for( var x=0; x<tempData.length; x++ ) 
                                        {
                                            try	
                                            {
                                                if(tempData[x].option.text != ' All' ) 
                                                {
                                                    if(!checkIfPresent(tmpRole, tempData[x].option))
                                                    tmpRole.add( tempData[x].option );
                                                }
                                            } catch(e)	{
                                            tmpRole.add( tempData[x].option, null );
                                            }
                                        }
                                    }
                                 }  
                            }
                        }                        
                    }
                }
            }
            var toBeAddedAgain = tmpSales.options[tmpSales.selectedIndex].value;
            tmpSales.remove(tmpSales.options.selectedIndex); //Remove the item from Source Listbox    
            addOption(tmpSales,toBeAddedAgain,toBeAddedAgain);
            if(toBeAddedAgain != ' All')
            {
                tempTextbox.value = tempTextbox.value + toBeAddedAgain;
                flag = flag + 1;
            }
            /*else if (toBeAddedAgain == ' All')
                flag = 1;*/
            else if (toBeAddedAgain == ' All')
            {
                tempTextbox.value = tempTextbox.value + toBeAddedAgain;
                flag = 1;
                break;
            }

        }
        sortSelect(tmpSales);
}
/****************************************************************************************
 *  Method for populating the Role on selection of SalesOrg for Group Administration
 *	data - this is the Select object.
 *	source - calling form element 
 ****************************************************************************************/	
function populateRoleTextBox( source ) {
    var tmpRole = document.getElementById( "role" );

    var tempTextbox = document.getElementById("selectedRole");
    tempTextbox.value = "";
    var flag = 1;
    
    while ( tmpRole.options.selectedIndex >= 0 ) 
    {
        if (flag != 1)
            tempTextbox.value = tempTextbox.value + ";";
            
        var toBeAddedAgain = tmpRole.options[tmpRole.selectedIndex].value;
        tmpRole.remove(tmpRole.options.selectedIndex); //Remove the item from Source Listbox    
        addOption(tmpRole,toBeAddedAgain,toBeAddedAgain);
        if(toBeAddedAgain != ' All')
        {
            tempTextbox.value = tempTextbox.value + toBeAddedAgain;
            flag = flag + 1;
        }
       /* else if (toBeAddedAgain == ' All')
                flag = 1;*/
        else if (toBeAddedAgain == ' All')
        {
            tempTextbox.value = tempTextbox.value + toBeAddedAgain;
            flag = 1;
            break;
        }
    }
    sortSelect(tmpRole);
}
/****************************************************************************************
 *  Method to check if the Option Item is already present in the ListBox
 *	selectObject - this is the ListBox object.
 *	option - Item to to checked in the ListBox object.
 ****************************************************************************************/	
function checkIfPresent(selectObject, option) {
    var intlength = selectObject.options.length;
    var flag = false;
    for(var j=0;j<intlength;j++) 
    {
        if (selectObject.options[j].text == option.text)
        {
            flag = true;
        }
    }
	return flag;
}
/****************************************************************************************
 *  Method to add an Option Item to the ListBox
 *	selectObject - this is the ListBox object.
 *	optionText - Item text to to added in the ListBox object.
 *	optionValue - Item value to to added in the ListBox object.
 ****************************************************************************************/	
function addOption(selectObject,optionText,optionValue) {
    var optionObject = new Option(optionText,optionValue)
    var optionRank = selectObject.options.length
    selectObject.options[optionRank]=optionObject
}
/****************************************************************************************
 *  Method to sort the items in the ListBox
 *	obj - this is the ListBox object.
 ****************************************************************************************/	
function sortSelect(obj) {
	var o = new Array();
	var count = obj.options.length;
	if (count <= 0) { return; }
	for (var i=0; i<count; i++) {
		o[o.length] = new Option( obj.options[i].text, obj.options[i].value, obj.options[i].defaultSelected, obj.options[i].selected) ;
	}
	if (o.length==0) { return; }
	o = o.sort( 
		function(a,b) { 
			if ((a.text+"") < (b.text+"")) { return -1; }
			if ((a.text+"") > (b.text+"")) { return 1; }
			return 0;
			} 
		);

	for (var i=0; i<o.length; i++) {
		obj.options[i] = new Option(o[i].text, o[i].value, o[i].defaultSelected, o[i].selected);
	}
}

	


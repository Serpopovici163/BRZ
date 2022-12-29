// This file contains a customized version of the Free Tigra Tree Javascript.
// The customizations allowed for features not available via the free JS 
    
/*
	Feel free to use your custom icons for the tree. Make sure they are all of the same size.
*/

var selPCD;
var isFatToc;
var pcdChanged;
var TREE_TPL = {
	'target'  : 'manual_frame',     // name of the frame links will be opened in
                                    // other possible values are: _blank, _parent, _search, _self and _top

	'icon_e'  : 'icons/empty.gif',  // empty image
	'icon_l'  : 'icons/empty.gif',  // vertical line
	
    'icon_32' : 'icons/folder.gif',   // root leaf icon normal
    'icon_36' : 'icons/foldersel.gif',   // root leaf icon selected
	
	'icon_48' : 'icons/folder.gif',   // root icon normal
	'icon_52' : 'icons/foldersel.gif',   // root icon selected
	'icon_56' : 'icons/folder.gif',   // root icon opened
	'icon_60' : 'icons/foldersel.gif',   // root icon selected
	
	'icon_16' : 'icons/folder.gif', // node icon normal
	'icon_20' : 'icons/foldersel.gif', // node icon selected
	'icon_24' : 'icons/folderopen.gif', // node icon opened
	'icon_28' : 'icons/foldersel.gif', // node icon selected opened

	'icon_0'  : 'icons/page.gif', // leaf icon normal
	'icon_4'  : 'icons/pagesel.gif', // leaf icon selected
	
	'icon_2'  : 'icons/empty.gif', // junction for leaf
	'icon_3'  : 'icons/empty.gif',       // junction for last leaf
	'icon_18' : 'icons/plus.gif', // junction for closed node
	'icon_19' : 'icons/plus.gif',       // junctioin for last closed node
	'icon_26' : 'icons/minus.gif',// junction for opened node
	'icon_27' : 'icons/minus.gif'       // junctioin for last opended node
};


// Title: Tigra Tree
// Description: See the demo at url
// URL: http://www.softcomplex.com/products/tigra_menu_tree/
// Version: 1.1
// Date: 11-12-2002 (mm-dd-yyyy)
// Notes: This script is free. Visit official site for further details.
function tree (a_items, a_template,selectedPCD,fatToc,pcdChange) {
	selPCD = selectedPCD;
	isFatToc = fatToc;
	pcdChanged=pcdChange;
	this.a_tpl      = a_template;
	this.a_config   = a_items;
	this.o_root     = this;
	this.a_index    = [];
	this.o_selected = null;
	this.n_depth    = -1;
	
	var o_icone = new Image(),
		o_iconl = new Image();
	o_icone.src = a_template['icon_e'];
	o_iconl.src = a_template['icon_l'];
	a_template['im_e'] = o_icone;
	a_template['im_l'] = o_iconl;
	for (var i = 0; i < 64; i++)
		if (a_template['icon_' + i]) {
			var o_icon = new Image();
			a_template['im_' + i] = o_icon;
			o_icon.src = a_template['icon_' + i];
		}
	
	this.toggle = function (n_id) {	var o_item = this.a_index[n_id]; o_item.open(o_item.b_opened) };
	this.select = function (n_id) { return this.a_index[n_id].select(); };
	this.mout   = function (n_id) { this.a_index[n_id].upstatus(true) };
	this.mover  = function (n_id) { this.a_index[n_id].upstatus() };
    
    // Added the following to allow the ability to open the tree to a specific node
    this.recursivetoggle = function(n_id) {var o_item = this.a_index[n_id];
    if(o_item.o_parent!=this){o_item.recursiveopen(o_item.b_opened)}else{this.toggle(n_id);}};

	this.a_children = [];
	for (var i = 0; i < a_items.length; i++)
		new tree_item(this, i);

	this.n_id = trees.length;
	trees[this.n_id] = this;
	
	for (var i = 0; i < this.a_children.length; i++) {
		document.write(this.a_children[i].init());
		this.a_children[i].open();
	}
}
function tree_item (o_parent, n_order){

	this.n_depth  = o_parent.n_depth + 1;
	this.a_config = o_parent.a_config[n_order + (this.n_depth ? 3 : 0)];
	if (!this.a_config) return;

	this.o_root    = o_parent.o_root;
	this.o_parent  = o_parent;
	this.n_order   = n_order;
	this.b_opened  = !this.n_depth;
	
	this.n_id = this.o_root.a_index.length;
	this.o_root.a_index[this.n_id] = this;
	o_parent.a_children[n_order] = this;

	this.a_children = [];
	for (var i = 0; i < this.a_config.length - 3; i++)
		new tree_item(this, i);

	this.get_icon = item_get_icon;
	this.open     = item_open;
	this.select   = item_select;
	this.init     = item_init;
	this.upstatus = item_upstatus;
	this.is_last  = function () { return this.n_order == this.o_parent.a_children.length - 1 };
	
    // Added line for recursive open feature - new feature to open tree to specific node
	this.recursiveopen = item_recopen;
}

function item_open (b_close) {
	var o_idiv = get_element('i_div' + this.o_root.n_id + '_' + this.n_id);
	if (!o_idiv) return;
	
	if (!o_idiv.innerHTML) {
		var a_children = [];
		for (var i = 0; i < this.a_children.length; i++)
			a_children[i]= this.a_children[i].init();
		o_idiv.innerHTML = a_children.join('');
	}
	o_idiv.style.display = (b_close ? 'none' : 'block');
	
	this.b_opened = !b_close;
	var o_jicon = document.images['j_img' + this.o_root.n_id + '_' + this.n_id],
		o_iicon = document.images['i_img' + this.o_root.n_id + '_' + this.n_id];
	if (o_jicon) o_jicon.src = this.get_icon(true);
	if (o_iicon) o_iicon.src = this.get_icon();
	this.upstatus();
}

function item_select (b_deselect) {
    if(this.a_config[1]==null || this.a_config[1]=="") 
        return false;
	if (!b_deselect) {
		var o_olditem = this.o_root.o_selected;
		this.o_root.o_selected = this;
		if (o_olditem) o_olditem.select(true);
	}
	var o_iicon = document.images['i_img' + this.o_root.n_id + '_' + this.n_id];
	if (o_iicon) o_iicon.src = this.get_icon();
	//get_element('i_txt' + this.o_root.n_id + '_' + this.n_id).style.fontWeight = b_deselect ? 'normal' : 'bold';
	get_element('i_txt' + this.o_root.n_id + '_' + this.n_id).className = b_deselect ? 'node' : 'nodeSel';
	 
	this.upstatus();
	return Boolean(this.a_config[1]);
}

function item_upstatus (b_clear) {
	//window.setTimeout('window.status="' + (b_clear ? '' : this.a_config[0] + (this.a_config[1] ? ' ('+ this.a_config[1] + ')' : '')) + '"', 10);
}

// Modified to display title and have folders open/close based on toggle of click
function item_init () {
	var a_offset = [],
		o_current_item = this.o_parent;
	for (var i = this.n_depth; i > 1; i--) {
		a_offset[i] = '<img alt="" src="' + this.o_root.a_tpl[o_current_item.is_last() ? 'icon_e' : 'icon_l'] + '" style="vertical-align:bottom; border:0;">';
		o_current_item = o_current_item.o_parent;
	}
	/*
	 * T300016897 - Starts To display the double quotes value during mouseover
	 */
	var modelyear = get("modelyear");
	var pcd = get("pcd");
	if(pcd == "" || pcd == "undefined" || pcd == "null" ||  typeof pcd == "undefined"){
		pcd = selPCD;
	}
	var hoverTitle = this.a_config[2];

	if (hoverTitle.indexOf("\"") != -1) {
		hoverTitle = hoverTitle.replace(/"/g, '&quot;');
	}
	
	var endLen = hoverTitle.length;
	if(endLen != -1){
		if(hoverTitle.indexOf("::") != -1){
			if(hoverTitle.indexOf("::") == endLen-2){
				hoverTitle = hoverTitle.substring(0,endLen-2);
			}
		}
	}
	
	if(isFatToc != "false" && (selPCD == "null" || (selPCD.indexOf("PDR") != -1))){
				document.getElementById("tree").disabled = true;
				document.getElementById("tree").style.opacity = 0.3;
				return '<a href="" name="ss'+this.n_id+'" id="ss'+this.n_id+'"></a><table cellpadding="0" cellspacing="0" border="0"><tr><td nowrap>' + (this.n_depth ? a_offset.join('') + (this.a_children.length
						? '<a title="' + hoverTitle + '"><img src="' + this.get_icon(true) + '" border="0" align="absbottom" name="j_img' + this.o_root.n_id + '_' + this.n_id + '"></a>'
						: (this.a_config[2].indexOf("::") != -1 ? '<a><img src="' + this.get_icon(true) + '" border="0" align="absbottom"></a>' : '<img src="icons/plus.gif" border="0" align="absbottom">')) : '') 
						+ '<a class="t' + this.o_root.n_id + 'i" id="i_txt' + this.o_root.n_id + '_' + this.n_id + '"><img src="icons/folder.gif" border="0" align="absbottom" name="i_img' + this.o_root.n_id + '_' + this.n_id + '" class="t' + this.o_root.n_id + 'im">' + this.a_config[0] + '</a></td></tr></table>' + (this.a_children.length ? '<div id="i_div' + this.o_root.n_id + '_' + this.n_id + '" style="display:none"></div>' : '');
		}
		if(this.a_config[1] != ""){
			this.a_config[1] = this.a_config[1]+"&pcdChangeBack="+pcdChanged+"&random="+Math.random(); //Rel 16.01 - Siviewer Defect fixes 
			if(isFatToc == "true"){
				if(this.a_config[1].indexOf("modelyear") == -1){
					this.a_config[1] = this.a_config[1]+"&pcd="+pcd+"&modelyear="+modelyear;
				}
			}
		}
		return '<a href="" name="ss'+this.n_id+'" id="ss'+this.n_id+'"></a><table cellpadding="0" cellspacing="0" border="0"><tr><td nowrap id="td'+this.n_id+'">' + (this.n_depth ? a_offset.join('') + (this.a_children.length
				? '<a href="javascript: trees[' + this.o_root.n_id + '].toggle(' + this.n_id + ')" title="' + hoverTitle + '" onmouseover="trees[' + this.o_root.n_id + '].mover(' + this.n_id + ')" onmouseout="trees[' + this.o_root.n_id + '].mout(' + this.n_id + ')"><img src="' + this.get_icon(true) + '" border="0" align="absbottom" name="j_img' + this.o_root.n_id + '_' + this.n_id + '"></a>'
				: (this.a_config[2].indexOf("::") != -1 ? '<a href="' + this.a_config[1] + '" title="' + hoverTitle + '" onclick="expandTreeSection(\''+this.a_config[1]+'\');"><img src="' + this.get_icon(true) + '" border="0" align="absbottom"></a>' : '<img src="' + this.get_icon(true) + '" border="0" align="absbottom">')) : '') 
				+ '<a href="' + this.a_config[1] + '" title="' + hoverTitle + '"' + (this.a_config[2].indexOf("::") != -1 ? '':' target="' + this.o_root.a_tpl['target'] + '"') + ' onclick="expandTreeSection(\''+this.a_config[1]+'\');parent.parent.control_frame.document.getElementById(\'objectType\').value=\'\';parent.parent.control_frame.document.getElementById(\'docTitle\').value =\'\';parent.parent.control_frame.document.getElementById(\'t3Id\').value =\'\';trees[' + this.o_root.n_id + '].toggle(' + this.n_id + ');return trees[' + this.o_root.n_id + '].select(' + this.n_id + ')" ondblclick="trees[' + this.o_root.n_id + '].toggle(' + this.n_id + ')" onmouseover="trees[' + this.o_root.n_id + '].mover(' + this.n_id + ')" onmouseout="trees[' + this.o_root.n_id + '].mout(' + this.n_id + ')" class="t' + this.o_root.n_id + 'i" id="i_txt' + this.o_root.n_id + '_' + this.n_id + '"><img src="' + this.get_icon() + '" border="0" align="absbottom" id="i_image' + this.o_root.n_id + '_' + this.n_id + '" name="i_img' + this.o_root.n_id + '_' + this.n_id + '" class="t' + this.o_root.n_id + 'im">' + this.a_config[0] + '</a></td></tr></table>' + (this.a_children.length ? '<div id="i_div' + this.o_root.n_id + '_' + this.n_id + '" style="display:none"></div>' : '');
	/*}else{
		//alert(startdate);
		document.getElementById("tree").disabled = true;
		document.getElementById("tree").style.opacity = 0.3;
		return '<a href="" name="ss'+this.n_id+'" id="ss'+this.n_id+'"></a><table cellpadding="0" cellspacing="0" border="0"><tr><td nowrap>' + (this.n_depth ? a_offset.join('') + (this.a_children.length
				? '<a title="' + this.a_config[2] + '"><img src="' + this.get_icon(true) + '" border="0" align="absbottom" name="j_img' + this.o_root.n_id + '_' + this.n_id + '"></a>'
				: (this.a_config[2].indexOf("::") != -1 ? '<a><img src="' + this.get_icon(true) + '" border="0" align="absbottom"></a>' : '<img src="' + this.get_icon(true) + '" border="0" align="absbottom">')) : '') 
				+ '<a class="t' + this.o_root.n_id + 'i" id="i_txt' + this.o_root.n_id + '_' + this.n_id + '"><img src="' + this.get_icon() + '" border="0" align="absbottom" name="i_img' + this.o_root.n_id + '_' + this.n_id + '" class="t' + this.o_root.n_id + 'im">' + this.a_config[0] + '</a></td></tr></table>' + (this.a_children.length ? '<div id="i_div' + this.o_root.n_id + '_' + this.n_id + '" style="display:none"></div>' : '');
	}*/
	}


	
function expandTreeSection(value)
{
	var isExpandedOnLoad = parent.control_frame.document.getElementById("isExpandedOnLoad").value;
	if(value.indexOf("nav") != -1){
		if(isExpandedOnLoad == ""){
			parent.control_frame.document.getElementById("isTreeExpanded").value = true; 
		}else{
			parent.control_frame.document.getElementById("isTreeExpanded").value = false; 
		}
		//parent.control_frame.document.getElementById('langChangeDocTitle').value =  get('docTitle');
	}
	if(value.indexOf('.html?') != -1){
		parent.control_frame.document.getElementById("isExpanded").value = "true";
		//parent.control_frame.document.getElementById('langChangeDocTitle').value =  get('docTitle');
	}
}

// Modified so that the second level node is always a folder even if no children - needed for Toyota Tree Structure
function item_get_icon (b_junction) {
	return this.o_root.a_tpl['icon_' + ((this.n_depth ? 0 : 32) + (this.a_children.length || this.a_config[2].indexOf("::") != -1 ? 16 : 0) + (this.a_children.length && this.b_opened ? 8 : 0) + (!b_junction && this.o_root.o_selected == this ? 0 : 0) + (b_junction ? 2 : 0) + (b_junction && this.is_last() ? 1 : 0))];
}

var trees = [];
get_element = document.all ?
	function (s_id) { return document.all[s_id] } :
	function (s_id) { return document.getElementById(s_id) };

// Customized functions below (added by JChiu: April 2006)
//
// Added function for recursive open of tree to specific node
function item_recopen(b_close) {
    if(this.o_parent!=this.o_root){
        this.o_parent.recursiveopen(b_close);
    }
    this.open(b_close);
    this.select(b_close);
}

// Customized function to expand all or collapse all for the tree
function expandCollapseTree(t, collapse) {
    for (var i = 1; i < t.a_index.length; i++) {
        var o_item = t.a_index[ i];
        o_item.open(collapse);
    }
} 

// Generic function to hide DIV tag in html
function hideDiv(pass) {
    var divs = document.getElementsByTagName('div');
    for(i=0;i<divs.length;i++){
        if(divs[i].id.match(pass)){//if they are 'see' divs
            if (document.getElementById) // DOM3 = IE5, NS6
                divs[i].style.visibility="hidden";// show/hide
            else
                if (document.layers) // Netscape 4
                    document.layers[divs[i]].display = 'hidden';
                else // IE 4
                    document.all.hideshow.divs[i].visibility = 'hidden';
        }
    }
}
    
// Generic function to show DIV tag in html
function showDiv(pass) {
    var divs = document.getElementsByTagName('div');
    for(i=0;i<divs.length;i++){
        if(divs[i].id.match(pass)){
            if (document.getElementById)
                divs[i].style.visibility="visible";
            else
                if (document.layers) // Netscape 4
                    document.layers[divs[i]].display = 'visible';
                else // IE 4
                    document.all.hideshow.divs[i].visibility = 'visible';
        }
    }
}     
    
// Function to get the selected index given an href string
function getSelectedIndex(t, href) {
	if(t.a_index){
	    for (var i = 1; i < t.a_index.length; i++) {
	        var o_item = t.a_index[ i];
	        if (o_item.a_config[1].indexOf(href) != -1) 
	            return selectedIndex = o_item.n_id;
	    }
	}
    return 0;
}

function get(name){
	var url = window.location.toString();
	var result = null;
	//get the parameters
	url.match(/\?(.+)$/); var params = RegExp.$1;
	// split up the query string and store in an // associative array
	var params = params.split("&"); 
	var queryStringList = {}; 
	for(var i=0;i<params.length;i++) {   
		var tmp = params[i].split("=");  
		if(name == tmp[0])
		{
			result = tmp[1];
		 return result;
		}}  
	}
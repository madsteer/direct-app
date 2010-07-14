<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<div class="contestDetail">

  <!-- Contest Description -->
	<div class="description">    
        <h3><span class="icon">Detailed Requirements</span><a href="javascript:;" class="helpIcon"><span class="hide">Help</span></a></h3>
        
        <div class="textarea2">
            <textarea id="swDetailedRequirements" rows="" cols=""></textarea>
        </div>        
        <div class="bottomTextarea">
        	<p>Describe what you want to accomplish.</p>
        	<span class="icon"></span>                                        
        </div>        
  </div>
    
  <!-- guidelines -->
	<div class="guidelines">    
        <h3><span class="icon">Software Guidelines</span><a href="javascript:;" class="helpIcon"><span class="hide">Help</span></a></h3>
        
        <div class="textarea2">
            <textarea id="swGuidelines" rows="" cols=""></textarea>
        </div>
        
        <div class="bottomTextarea">
        	<p>Submission Deliverables?, Environment Setup Instructions?, Final Submission Guidelines?</p>
        	<span class="icon"></span>                                        
        </div>
        
  </div>
  <!-- end .guidelines -->
        
  <div class="prizes" id="swTechnologyDiv">    
        <h3>Choose Your Technology:</h3>
        
        <div class="prizesInner_tech">
        <span class="head_font">Master Technologies</span>
        <span class="head_font_space">Your Project Technologies</span>
        <br />	
        <select multiple id="masterTechnologiesSelect">  
            <s:iterator value="referenceDataBean.technologies">
               <option value='<s:property value="id" />'><s:property value="name" /></option>
            </s:iterator>                  	
   	    </select>  
   	    
        <div id="button_tech">
        	<img src="/images/add_tech.png" alt="add" id="addTechnologies" />
            <br /><br />
          <img src="/images/remove_tech.png" alt="remove" id="removeTechnologies" />
        </div>    
        
        <select multiple id="masterTechnologiesChoosenSelect">
        </select>   
      </div>
   </div>
         
   <div class="prizes" id="swCatalogDiv">
     <div class="catalogSelect">
				<label>Catalog Name:</label>
        <select id="catalogSelect">
        		<option value='-1'>&nbsp;</option>
        	<s:iterator value="referenceDataBean.catalogs"> 
        	  <option value='<s:property value="id" />'><s:property value="name" /></option>
        	</s:iterator>
        </select>	
     </div> <!-- end of catalogSelect -->
			<div class="prizesInner_tech">
				<span class="head_font">Master Categories</span>
				<span class="head_font_space_categories">Your Project Categories</span>
				<br />
				<select multiple="multiple" id="select1_categories">					
				</select>
				<div id="button_tech">
					<img src="/images/add_tech.png" alt="add" id="addCategories" />
					<br />
					<br />
					<img src="/images/remove_tech.png" alt="remove" id="removeCategories" />
				</div>
				<select multiple="multiple" id="select2_categories">
				</select>
			</div> <!-- end of prizesInner_tech -->
		</div>
										                                        
</div>
<!-- Contest Detail -->
 
  <!-- upload -->
	<div id="swUploadSection">
    
        <h3>File Upload: </h3>       
        <div class="uploadInner">
            <div id="swDocumentList">
            </div>
            <div>
               File to Upload: <span id="swUploadButtonDiv"><input name="document" type="file" > </span> <br/>
               File Description:
               <textarea id="swFileDescription" rows="5" cols="50"></textarea>               
               <input id="swFileUploadBtn"  type="button" value="Upload File -->" /> <br/>
               <input id="swSpecDoc" type="checkbox" class="reqDocCheck" checked="true" /> <span class="reqDocCheck">Requirements Specification</span>
            </div>
        </div> <!-- end .uploadInner -->

        <div id="swFileTemplte" class="hide">
         <div id="doc{0}" class="document">
           <span class="fileInput">{1}</span>
           <a href="javascript:swRemoveDocument({0});" >remove</a> 
           <span id="doc{0}spec" class="hide"> (Requirements Specification) </span>
         </div>
       </div>  
        
 </div>
 <!-- end #uploadSection -->

<!-- Add Links -->
<div class="upload">                                    
    <h3>Add Links:</h3>
    
    <div class="uploadInner">
    	<input type="text" class="text uploadInput" value="Link Name" />
        <input type="file" class="fileIn hide" />
        
        <a href="javascript:alert('it will be done in next assembly');" class="removeButton"><span class="hide">REMOVE</span></a>
        <a href="javascript:alert('it will be done in next assembly');" class="addButton"><span class="hide">ADD</span></a>
    </div>    
</div>
                                    
<!-- prizes -->
<div class="prizes">  
      <h3>Contest Prizes:</h3>      
      <div class="prizesInner_software">
      	<span class="head"><p>Please Select the prize structure for your contest by choosing one of the options 
          below:</p></span> 
          <p>
          <br />
          <span class="radio_font">
      	  <input type="radio" name="prizeRadio" value="low" />&nbsp;&nbsp;&nbsp;Low&nbsp;&nbsp;($<span id="swPrize_low"></span>)
          <input type="radio" name="prizeRadio" value="medium" class="space_radio" />&nbsp;&nbsp;&nbsp;Medium&nbsp;&nbsp;($<span id="swPrize_medium"></span>)
          <input type="radio" name="prizeRadio" value="high" class="space_radio" />&nbsp;&nbsp;&nbsp;High&nbsp;&nbsp;($<span id="swPrize_high"></span>)
          <input type="radio" name="prizeRadio" value="custom" class="space_radio customRadio" /> <span class="customRadio"> &nbsp;&nbsp;&nbsp; Custom </span>
          </span>
          
          </p>
          <br />
          <div class="prizesInner">
        	<label class="first">1st Place</label>
          <span class="dw">$</span>
          <input type="text" class="prizesInput" value="" id="swFirstPlace" readonly="true" />
          <label class="second">2nd Place</label>
          <span class="dw">$</span>
          <span id="swSecondPlace" class="mid_info"></span>
          
      	</div>
          <br />
          <span class="head">
          <span class="first_info">Review Cost:&nbsp;&nbsp;$ <span id="swReviewCost"></span></span>
          <span class="mid_info">Reliability Bonus:&nbsp;&nbsp;$ <span id="swReliabilityBonus"></span></span>
          <span class="mid_info">Digital Run:&nbsp;&nbsp;$  <span id="swDigitalRun"></span></span>
          <span class="mid_info">Contest Fee:&nbsp;&nbsp;$  <span id="swContestFee"></span></span>
          <span class="last_info"><strong>Contest Total:&nbsp;&nbsp;$  <span id="swTotal"></span></strong></span>
          </span>                                                                
      </div>
  </div>
  <!-- end .prizes -->
                                    
    <div class="buttonBox">
        <a href="javascript:continueOverview();" class="button6 contiune"><span class="left"><span class="right">CONTINUE</span></span></a>
        <a href="javascript:saveAsDraftOverview();" class="button6 draft"><span class="left"><span class="right">SAVE AS DRAFT</span></span></a>
        <a href="javascript:previewContest();" class="button6 preview"><span class="left"><span class="right">PREVIEW</span></span></a>
        <a href="javascript:cancelContest();" class="button6 preview"><span class="left"><span class="right">CANCEL</span></span></a>
        <a href="javascript:backOverview();" class="button6 preview"><span class="left"><span class="right">BACK</span></span></a>
    </div>
                                    
                       
<%--
  - Author: TCSASSEMBLER
  - Version: 1.0
  - Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: Launch copilot contest page input/edit section.
  - Since: TC Direct - Launch Copilot Selection Contest assembly  
--%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<!-- add new contest -->
<div class="addNewContestSection">
    <div class="addNewContest">
        <div class="editPanelMask">
            <a class="hide" href="javascript:;" ><img class="edit_type" alt="edit" src="/images/edit_red.png" /></a>
            <!-- Project Name -->
            <div class="row">
            <label>Select the project that needs a Copilot :</label>
                <div class="projectSelect">
                    <select id="projects" name="tcProject">
                        <option value="-1">Please select an existing project</option>
                        <s:iterator value="projects" var="proj">
                        <option value='<s:property value="projectId" />'  <s:if test="%{#proj.projectId==#session.currentProject.id}">selected="selected"</s:if> ><s:property value="name"/>
                        </option>
                        </s:iterator>
                    </select>
                </div>
                <div class="addNew">
                    <a href="javascript:;" class="button6" id="addNewProject"><span class="left"><span class="right">ADD NEW</span></span></a>
                </div>
            </div>
            
            <!-- Contest Name -->
            <div class="row">
                <label>Create a name for your Copilot Posting :</label>
                <input id="contestName" name="contestName" type="text" class="text" maxlength="254"/>
                <input id="contestNameFromDesign" name="contestNameFromDesign" type="text" class="text hide" maxlength="300" />          
                <input id="contestIdFromDesign" name="contestIdFromDesign" type="hidden" />
                <div id="devOnlyDiv" class="lcc_chk hide" style="float:none;">
                    <input id="devOnlyCheckBox" type="checkbox" />
                    <span class="lcc">Dev Only</span>
                </div>
            </div>
            
            <!-- Billing Account -->
            <div class="row">
                <label>Billing Account :</label>
                <div class="billingSelect">
                    <select id="billingProjects" name="billingProject">
                        <option value="0">Please select an existing account</option>
                        <s:iterator value="billingProjects">
                        <option value='<s:property value="projectId" />'><s:property value="name" /></option>
                        </s:iterator>
                    </select>
                </div>
            </div>    
            
            <p class="save hide"> 
                <a class="cancel_text" href="javascript:;">cancel</a>
                <a href="javascript:;"><img class="save_btn" alt="save" src="/images/save_change.png" /></a>
            </p>
        </div>
    </div>
    
    <div class="addNewContestInfo infoPanel hide">
        <div class="infoPanelMask">
            <ul>
                <li>
                    <label>Copilot Posting Name :</label>
                    <label id="copilotPostingNameEdit" class="textValue"></label>
                </li>
                <li>
                    <label>Project Name :</label>
                    <label id="projectNameEdit" class="textValue"></label>
                </li>
                <li>
                    <label>Billing Account :</label>
                    <label id="billingAccountEdit" class="textValue"></label>
                </li>
            </ul>
            <a href="javascript:;" class="editLink"><img class="edit_type" alt="edit" src="/images/edit.png" /></a>
        </div> 
    </div>
</div>
<!-- end .addNewContest -->

<div class="scheduleSection">
    <div class="iconDivToHide">
        <h3><span class="icon">Schedule</span></h3>    
    </div>
    <!-- Contest Schedule --> 
    <div class="schedule">
        <div class="iconDiv hide">
            <h3><span class="icon">Schedule</span>
            <a href="javascript:;" class="editLink"><img class="edit_type" alt="edit" src="/images/edit_red.png" /></a></h3>
        </div>
        <div class="editPanelMask">
            <!-- Start -->
            <div class="row">
            <label>Start:</label>
                <input id="startDate" name="startDate" type="text"  class="text date-pick" readonly="true"/>
                <div class="startEtSelect">
                    <select id="startTime" name="startTime" ><jsp:include page="../common/timeOptions.jsp"/></select>
                </div>
                <span>ET (GMT-04)</span>
            </div>
            <p class="save hide">
                <a class="cancel_text" href="javascript:;">cancel</a>
                <a href="javascript:;"><img class="save_btn" alt="save" src="/images/save_change.png" /></a>
            </p>            
        </div>
    </div>
    
    <div class="infoPanel scheduleInfo hide"> 
        <h3><span class="icon">Schedule</span><a href="javascript:;" class="editLink"><img class="edit_type" alt="edit" src="/images/edit.png" /></a></h3>
        <div class="infoPanelMask">
            <ul>
                <li>
                    <label>Start :</label> 
                    <label id="startDateEdit" class="textValue"></label>
                </li>
            </ul>
        </div>    
    </div>
</div>
<!-- end .schedule -->

<!-- Contest Description -->
<div class="allDescriptionSection">
    <div class="iconDivToHide">
        <h3><span class="icon">Enter a description that you want everyone to see</span><a href="javascript:;" class="helpIcon tooltipLink" id="allDescriptionIcon"><span class="hide">Help</span></a></h3>
    </div>
    <div class="description"> 
        <div class="iconDiv hide">
            <h3><span class="icon">Enter a description that you want everyone to see</span><a href="javascript:;" ><img class="edit_type" alt="edit" src="/images/edit_red.png" /></a></h3>
        </div>
        <div class="editPanelMask">
            <div class="textarea">
               <textarea rows="15" cols="80" style="width:100%;" id="allDescription"></textarea>
            </div> 
            
            <p class="save hide">
                <a class="cancel_text" href="javascript:;">cancel</a>
                <a href="javascript:;"><img class="save_btn" alt="save" src="/images/save_change.png" /></a>
            </p>            
        </div>
    </div>
    
     <div class="infoPanel descriptionInfo hide">
            <h3><span class="icon">Description that you want everyone to see</span><a href="javascript:;" class="editLink"><img class="edit_type" alt="edit" src="/images/edit.png" /></a></h3>
            <div class="infoPanelMask">
                <p id="allDescriptionEdit"></p>
            </div> 
     </div>    
</div>
<!-- end .description -->

<!-- Contest Description -->
<div class="privateDescriptionSection">
    <div class="iconDivToHide">
        <h3><span class="icon">Enter a description that is only viewable to copilots that register for this posting</span><a href="javascript:;" class="helpIcon tooltipLink" id="privateDescriptionIcon"><span class="hide">Help</span></a></h3>
    </div>
    <div class="description"> 
        <div class="iconDiv hide">
            <h3><span class="icon">Enter a description that is only viewable to copilots that register for this posting</span><a href="javascript:;" ><img class="edit_type" alt="edit" src="/images/edit_red.png" /></a></h3>
        </div>   
        <div class="editPanelMask">        
            <div class="textarea">
                <textarea rows="15" cols="80" style="width:100%;" id="privateDescription"></textarea>
            </div> 
            
            <p class="save hide">
                <a class="cancel_text" href="javascript:;">cancel</a>
                <a href="javascript:;"><img class="save_btn" alt="save" src="/images/save_change.png" /></a>
            </p>         
        </div>
    </div>
    
    <div class="infoPanel descriptionInfo hide">
            <h3><span class="icon">Description that is only viewable to copilots that register for this posting</span><a href="javascript:;" class="editLink"><img class="edit_type" alt="edit" src="/images/edit.png" /></a></h3>
            <div class="infoPanelMask">
                <p id="privateDescriptionEdit"></p>
            </div> 
     </div>     
</div>
<!-- end .description -->

 <!-- Contest Costs -->
<div class="costs editMask">
    <h3><span class="icon">Costs</span><a href="javascript:;" class="helpIcon tooltipLink" id="costIcon"><span class="hide">Help</span></a></h3>
    <table class="prizesTable">
        <tbody><tr>
            <td class="prize"><span>Administration Fee:</span> $<span id="sworAdminFee" class="feeValue"></span> </td>
            <td class="prize"><span>1st Prize :</span> $<span id="sworFirstFee" class="feeValue"></span> </td>
            <td class="prize"><span>2nd Prize :</span> $<span id="sworSecondFee" class="feeValue"></span></td>
            <td class="total">Total</td>
            <td class="last" id="sworTotal"></td>
        </tr>
        </tbody>
    </table>
    
    <!-- only used for updateSoftwarePrizes method, will not be shown -->
    <div class="hide">
        <input type="radio" name="prizeRadio" value="low" checked="true"></input>
        <input type="radio" name="prizeRadio" value="medium"></input>
        <input type="radio" name="prizeRadio" value="high"></input>
    </div>
</div>
<!-- end .Costs -->

<!-- upload -->
<div class="uploadSection">
    <div class="iconDivToHide">
        <h3><span class="icon">File Upload</span></h3>
    </div>    
    <div id="swUploadSection" class="fileupload">
        <div class="iconDiv hide">
            <h3><span class="icon">File Upload</span><a href="javascript:;" ><img class="edit_type" alt="edit" src="/images/edit_red.png" /></a></h3>
        </div>         
        <div class="editPanelMask">        
            <div class="uploadInner">
                <table id="swDocumentList" style="width:100%">
                </table>
                <div>
                   File to Upload: <span id="swUploadButtonDiv"><input name="document" type="file" > </span> <br/>
                   File Description:
                   <textarea id="swFileDescription" rows="5" cols="50"></textarea>               
                   <input id="swFileUploadBtn"  type="button" value="Upload File" /> <br/>
                </div>
            </div> <!-- end .uploadInner -->

            <table id="swFileTemplte" class="hide">
                <tr id="doc{0}" class="document">
                    <td style="width:40%"><span class="fileInput">{1}</span></td>
                    <td style="width:40%"><span class="fileInput">{2}</span></td>
                    <td style="width:20%"><a href="javascript:swRemoveDocument({0});" >remove</a> </td>
                </tr>
            </table>
            
            <p class="save hide">
                <a href="javascript:;"><img class="save_btn" alt="save" src="/images/save_change.png" /></a>
            </p> 
        </div>       
    </div>
    
    <div class="infoPanel fileuploadInfo hide">
         <h3><span class="icon">File Upload</span><a href="javascript:;" class="editLink"><img class="edit_type" alt="edit" src="/images/edit.png" /></a></h3>
         <div class="infoPanelMask">   
            <table id="fileUploadTableEdit">
            </table> 
        </div>     
    </div>     
</div>
<!-- end #uploadSection -->

<div class="bottomButton"> 
        <a class="conditions thickbox" href="/direct/conditions.jsp?height=400&amp;width=800&amp;inlineId=helpPopup">Terms and Conditions</a>
        <a id="continue" href="javascript:;" class="button6 contiune"><span class="left"><span class="right">CONTINUE</span></span></a>
        <a id="submitButton" href="javascript:;" class="button6 contiune hide"><span class="left"><span class="right">SUBMIT &amp; LAUNCH CONTEST</span></span></a>
        <a id="saveDraftButton" href="javascript:;" class="button6 draft"><span class="left"><span class="right">SAVE AS DRAFT</span></span></a>                           
</div>

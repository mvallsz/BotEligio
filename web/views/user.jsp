<script type="text/javascript" src="js/jquery.itextclear.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $('input[type=text], input[type=password], input[type=url], input[type=email], input[type=number], textarea', '.form').iTextClear();
});
</script>
                <h1 class="page-title">Form Sample</h1>
                <div class="container_12 clearfix leading">
                    <div class="grid_12">
                    	<form class="form has-validation">

                            <div class="clearfix">

                                <label for="form-name" class="form-label">Name <em>*</em></label>

                                <div class="form-input"><input type="text" id="form-name" name="name" required="required" placeholder="Enter your name" /></div>

                            </div>

                            <div class="clearfix">

                                <label for="form-email" class="form-label">Email <em>*</em></label>

                                <div class="form-input"><input type="email" id="form-email" required="required" placeholder="A valid email address" /></div>

                            </div>

                            <div class="clearfix">

                                <label for="form-username" class="form-label">Username <em>*</em></label>

                                <div class="form-input"><input type="text" id="form-username" name="username" required="required" maxlength="12" placeholder="Alphanumeric (max 12 char.)" /></div>

                            </div>

                            <div class="clearfix">

                                <label for="form-password" class="form-label">Password</label>

                                <div class="form-input"><input type="password" id="form-password" name="password" maxlength="30" placeholder="max. 30 char." /></div>

                            </div>

                            <div class="clearfix">

                                <label for="form-password-check" class="form-label">Password check</label>

                                <div class="form-input"><input type="password" id="form-password-check" name="check" data-equals="password" maxlength="30" placeholder="Re-enter your password" /></div>

                            </div>

                            <div class="form-action clearfix">

                                <button class="button" type="submit">OK</button>

                                <button class="button" type="reset">Reset</button>

                            </div>

                        </form>
                    </div>
                </div>
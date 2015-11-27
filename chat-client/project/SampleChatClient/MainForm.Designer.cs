namespace SampleChatClient
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.gbLogin = new System.Windows.Forms.GroupBox();
            this.btnLogout = new System.Windows.Forms.Button();
            this.btnLogin = new System.Windows.Forms.Button();
            this.edtLoginUrl = new System.Windows.Forms.TextBox();
            this.lblLoginUrl = new System.Windows.Forms.Label();
            this.edtUserName = new System.Windows.Forms.TextBox();
            this.lblUserName = new System.Windows.Forms.Label();
            this.gbLobby = new System.Windows.Forms.GroupBox();
            this.listRooms = new System.Windows.Forms.ListBox();
            this.btnEnterRoom = new System.Windows.Forms.Button();
            this.btnCreateRoom = new System.Windows.Forms.Button();
            this.gbChatRoom = new System.Windows.Forms.GroupBox();
            this.ckbPrivate = new System.Windows.Forms.CheckBox();
            this.cbbChatToWho = new System.Windows.Forms.ComboBox();
            this.lblTo = new System.Windows.Forms.Label();
            this.btnLeave = new System.Windows.Forms.Button();
            this.btnSend = new System.Windows.Forms.Button();
            this.edtChat = new System.Windows.Forms.TextBox();
            this.listUsers = new System.Windows.Forms.ListBox();
            this.richChatBox = new System.Windows.Forms.RichTextBox();
            this.timerRefreshRooms = new System.Windows.Forms.Timer(this.components);
            this.timerAutoRelogin = new System.Windows.Forms.Timer(this.components);
            this.gbLogin.SuspendLayout();
            this.gbLobby.SuspendLayout();
            this.gbChatRoom.SuspendLayout();
            this.SuspendLayout();
            // 
            // gbLogin
            // 
            this.gbLogin.Controls.Add(this.btnLogout);
            this.gbLogin.Controls.Add(this.btnLogin);
            this.gbLogin.Controls.Add(this.edtLoginUrl);
            this.gbLogin.Controls.Add(this.lblLoginUrl);
            this.gbLogin.Controls.Add(this.edtUserName);
            this.gbLogin.Controls.Add(this.lblUserName);
            this.gbLogin.Location = new System.Drawing.Point(12, 12);
            this.gbLogin.Name = "gbLogin";
            this.gbLogin.Size = new System.Drawing.Size(640, 68);
            this.gbLogin.TabIndex = 0;
            this.gbLogin.TabStop = false;
            this.gbLogin.Text = "Login";
            // 
            // btnLogout
            // 
            this.btnLogout.Location = new System.Drawing.Point(588, 32);
            this.btnLogout.Name = "btnLogout";
            this.btnLogout.Size = new System.Drawing.Size(48, 23);
            this.btnLogout.TabIndex = 5;
            this.btnLogout.Text = "Logout";
            this.btnLogout.UseVisualStyleBackColor = true;
            this.btnLogout.Visible = false;
            this.btnLogout.Click += new System.EventHandler(this.btnLogout_Click);
            // 
            // btnLogin
            // 
            this.btnLogin.Location = new System.Drawing.Point(482, 32);
            this.btnLogin.Name = "btnLogin";
            this.btnLogin.Size = new System.Drawing.Size(100, 23);
            this.btnLogin.TabIndex = 4;
            this.btnLogin.Text = "Login";
            this.btnLogin.UseVisualStyleBackColor = true;
            this.btnLogin.Click += new System.EventHandler(this.btnLogin_Click);
            // 
            // edtLoginUrl
            // 
            this.edtLoginUrl.Location = new System.Drawing.Point(196, 33);
            this.edtLoginUrl.Name = "edtLoginUrl";
            this.edtLoginUrl.Size = new System.Drawing.Size(271, 22);
            this.edtLoginUrl.TabIndex = 3;
            this.edtLoginUrl.Text = "http://127.0.0.1:11091";
            // 
            // lblLoginUrl
            // 
            this.lblLoginUrl.AutoSize = true;
            this.lblLoginUrl.Location = new System.Drawing.Point(194, 18);
            this.lblLoginUrl.Name = "lblLoginUrl";
            this.lblLoginUrl.Size = new System.Drawing.Size(59, 12);
            this.lblLoginUrl.TabIndex = 2;
            this.lblLoginUrl.Text = "Login URL";
            // 
            // edtUserName
            // 
            this.edtUserName.Location = new System.Drawing.Point(51, 33);
            this.edtUserName.Name = "edtUserName";
            this.edtUserName.Size = new System.Drawing.Size(125, 22);
            this.edtUserName.TabIndex = 1;
            this.edtUserName.Text = "tom";
            // 
            // lblUserName
            // 
            this.lblUserName.AutoSize = true;
            this.lblUserName.Location = new System.Drawing.Point(49, 18);
            this.lblUserName.Name = "lblUserName";
            this.lblUserName.Size = new System.Drawing.Size(56, 12);
            this.lblUserName.TabIndex = 0;
            this.lblUserName.Text = "User Name";
            // 
            // gbLobby
            // 
            this.gbLobby.Controls.Add(this.listRooms);
            this.gbLobby.Controls.Add(this.btnEnterRoom);
            this.gbLobby.Controls.Add(this.btnCreateRoom);
            this.gbLobby.Location = new System.Drawing.Point(12, 86);
            this.gbLobby.Name = "gbLobby";
            this.gbLobby.Size = new System.Drawing.Size(165, 333);
            this.gbLobby.TabIndex = 1;
            this.gbLobby.TabStop = false;
            this.gbLobby.Text = "Lobby";
            // 
            // listRooms
            // 
            this.listRooms.FormattingEnabled = true;
            this.listRooms.ItemHeight = 12;
            this.listRooms.Location = new System.Drawing.Point(6, 79);
            this.listRooms.Name = "listRooms";
            this.listRooms.Size = new System.Drawing.Size(150, 244);
            this.listRooms.TabIndex = 2;
            this.listRooms.DoubleClick += new System.EventHandler(this.listRooms_DoubleClick);
            // 
            // btnEnterRoom
            // 
            this.btnEnterRoom.Location = new System.Drawing.Point(6, 50);
            this.btnEnterRoom.Name = "btnEnterRoom";
            this.btnEnterRoom.Size = new System.Drawing.Size(150, 23);
            this.btnEnterRoom.TabIndex = 1;
            this.btnEnterRoom.Text = "Enter Selected Room";
            this.btnEnterRoom.UseVisualStyleBackColor = true;
            this.btnEnterRoom.Click += new System.EventHandler(this.btnEnterRoom_Click);
            // 
            // btnCreateRoom
            // 
            this.btnCreateRoom.Location = new System.Drawing.Point(6, 21);
            this.btnCreateRoom.Name = "btnCreateRoom";
            this.btnCreateRoom.Size = new System.Drawing.Size(150, 23);
            this.btnCreateRoom.TabIndex = 0;
            this.btnCreateRoom.Text = "Create New Room";
            this.btnCreateRoom.UseVisualStyleBackColor = true;
            this.btnCreateRoom.Click += new System.EventHandler(this.btnCreateRoom_Click);
            // 
            // gbChatRoom
            // 
            this.gbChatRoom.Controls.Add(this.ckbPrivate);
            this.gbChatRoom.Controls.Add(this.cbbChatToWho);
            this.gbChatRoom.Controls.Add(this.lblTo);
            this.gbChatRoom.Controls.Add(this.btnLeave);
            this.gbChatRoom.Controls.Add(this.btnSend);
            this.gbChatRoom.Controls.Add(this.edtChat);
            this.gbChatRoom.Controls.Add(this.listUsers);
            this.gbChatRoom.Controls.Add(this.richChatBox);
            this.gbChatRoom.Location = new System.Drawing.Point(183, 86);
            this.gbChatRoom.Name = "gbChatRoom";
            this.gbChatRoom.Size = new System.Drawing.Size(469, 333);
            this.gbChatRoom.TabIndex = 2;
            this.gbChatRoom.TabStop = false;
            this.gbChatRoom.Text = "Room";
            // 
            // ckbPrivate
            // 
            this.ckbPrivate.AutoSize = true;
            this.ckbPrivate.Location = new System.Drawing.Point(292, 303);
            this.ckbPrivate.Name = "ckbPrivate";
            this.ckbPrivate.Size = new System.Drawing.Size(56, 16);
            this.ckbPrivate.TabIndex = 7;
            this.ckbPrivate.Text = "Private";
            this.ckbPrivate.UseVisualStyleBackColor = true;
            // 
            // cbbChatToWho
            // 
            this.cbbChatToWho.FormattingEnabled = true;
            this.cbbChatToWho.Location = new System.Drawing.Point(224, 301);
            this.cbbChatToWho.Name = "cbbChatToWho";
            this.cbbChatToWho.Size = new System.Drawing.Size(62, 20);
            this.cbbChatToWho.TabIndex = 6;
            // 
            // lblTo
            // 
            this.lblTo.AutoSize = true;
            this.lblTo.Location = new System.Drawing.Point(204, 307);
            this.lblTo.Name = "lblTo";
            this.lblTo.Size = new System.Drawing.Size(14, 12);
            this.lblTo.TabIndex = 5;
            this.lblTo.Text = "to";
            // 
            // btnLeave
            // 
            this.btnLeave.Location = new System.Drawing.Point(354, 270);
            this.btnLeave.Name = "btnLeave";
            this.btnLeave.Size = new System.Drawing.Size(108, 23);
            this.btnLeave.TabIndex = 4;
            this.btnLeave.Text = "Leave";
            this.btnLeave.UseVisualStyleBackColor = true;
            this.btnLeave.Click += new System.EventHandler(this.btnLeave_Click);
            // 
            // btnSend
            // 
            this.btnSend.Location = new System.Drawing.Point(354, 299);
            this.btnSend.Name = "btnSend";
            this.btnSend.Size = new System.Drawing.Size(108, 23);
            this.btnSend.TabIndex = 3;
            this.btnSend.Text = "Send";
            this.btnSend.UseVisualStyleBackColor = true;
            this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
            // 
            // edtChat
            // 
            this.edtChat.Location = new System.Drawing.Point(12, 301);
            this.edtChat.Name = "edtChat";
            this.edtChat.Size = new System.Drawing.Size(186, 22);
            this.edtChat.TabIndex = 2;
            // 
            // listUsers
            // 
            this.listUsers.FormattingEnabled = true;
            this.listUsers.ItemHeight = 12;
            this.listUsers.Location = new System.Drawing.Point(354, 21);
            this.listUsers.Name = "listUsers";
            this.listUsers.Size = new System.Drawing.Size(109, 244);
            this.listUsers.TabIndex = 1;
            this.listUsers.DoubleClick += new System.EventHandler(this.listUsers_DoubleClick);
            // 
            // richChatBox
            // 
            this.richChatBox.Location = new System.Drawing.Point(12, 21);
            this.richChatBox.Name = "richChatBox";
            this.richChatBox.Size = new System.Drawing.Size(336, 274);
            this.richChatBox.TabIndex = 0;
            this.richChatBox.Text = "";
            // 
            // timerRefreshRooms
            // 
            this.timerRefreshRooms.Enabled = true;
            this.timerRefreshRooms.Interval = 9000;
            this.timerRefreshRooms.Tick += new System.EventHandler(this.timerRefreshRooms_Tick);
            // 
            // timerAutoRelogin
            // 
            this.timerAutoRelogin.Interval = 2000;
            this.timerAutoRelogin.Tick += new System.EventHandler(this.timerAutoRelogin_Tick);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(664, 442);
            this.Controls.Add(this.gbChatRoom);
            this.Controls.Add(this.gbLobby);
            this.Controls.Add(this.gbLogin);
            this.Name = "MainForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Chat Client";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
            this.Load += new System.EventHandler(this.MainForm_Load);
            this.gbLogin.ResumeLayout(false);
            this.gbLogin.PerformLayout();
            this.gbLobby.ResumeLayout(false);
            this.gbChatRoom.ResumeLayout(false);
            this.gbChatRoom.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox gbLogin;
        private System.Windows.Forms.Label lblUserName;
        private System.Windows.Forms.TextBox edtUserName;
        private System.Windows.Forms.Label lblLoginUrl;
        private System.Windows.Forms.TextBox edtLoginUrl;
        private System.Windows.Forms.Button btnLogin;
        private System.Windows.Forms.GroupBox gbLobby;
        private System.Windows.Forms.Button btnCreateRoom;
        private System.Windows.Forms.Button btnEnterRoom;
        private System.Windows.Forms.ListBox listRooms;
        private System.Windows.Forms.GroupBox gbChatRoom;
        private System.Windows.Forms.ListBox listUsers;
        private System.Windows.Forms.RichTextBox richChatBox;
        private System.Windows.Forms.TextBox edtChat;
        private System.Windows.Forms.Button btnSend;
        private System.Windows.Forms.Button btnLeave;
        private System.Windows.Forms.Label lblTo;
        private System.Windows.Forms.ComboBox cbbChatToWho;
        private System.Windows.Forms.CheckBox ckbPrivate;
        private System.Windows.Forms.Button btnLogout;
        private System.Windows.Forms.Timer timerRefreshRooms;
        private System.Windows.Forms.Timer timerAutoRelogin;
    }
}


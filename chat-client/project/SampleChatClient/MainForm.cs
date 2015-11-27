using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Windows.Forms;

using SharpNetwork;
using SharpNetwork.SimpleWebSocket;

namespace SampleChatClient
{
    public partial class MainForm : Form, ChatClient
    {
        MessageClient m_Client = null;

        string m_LoginURL = "";

        string m_UserName = "";
        string m_UserToken = "";

        string m_RoomName = "";

        List<string> m_UserList = new List<string>();

        public MainForm()
        {
            InitializeComponent();
        }

        public static void DisableEditing(ComboBox cbb)
        {
            cbb.KeyPress += delegate(object sender, KeyPressEventArgs e)
            {
                e.Handled = true;
            };
        }

        public static DialogResult InputBox(string title, string promptText, ref string value)
        {
            Form form = new Form();
            Label label = new Label();
            TextBox textBox = new TextBox();
            Button buttonOk = new Button();
            Button buttonCancel = new Button();

            form.Text = title;
            label.Text = promptText;
            textBox.Text = value;

            buttonOk.Text = "OK";
            buttonCancel.Text = "Cancel";
            buttonOk.DialogResult = DialogResult.OK;
            buttonCancel.DialogResult = DialogResult.Cancel;

            label.SetBounds(9, 20, 372, 13);
            textBox.SetBounds(12, 36, 372, 20);
            buttonOk.SetBounds(228, 72, 75, 23);
            buttonCancel.SetBounds(309, 72, 75, 23);

            label.AutoSize = true;
            textBox.Anchor = textBox.Anchor | AnchorStyles.Right;
            buttonOk.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
            buttonCancel.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;

            form.ClientSize = new Size(396, 107);
            form.Controls.AddRange(new Control[] { label, textBox, buttonOk, buttonCancel });
            form.ClientSize = new Size(Math.Max(300, label.Right + 10), form.ClientSize.Height);
            form.FormBorderStyle = FormBorderStyle.FixedDialog;
            form.StartPosition = FormStartPosition.CenterScreen;
            form.MinimizeBox = false;
            form.MaximizeBox = false;
            form.AcceptButton = buttonOk;
            form.CancelButton = buttonCancel;

            DialogResult dialogResult = form.ShowDialog();
            value = textBox.Text;
            return dialogResult;
        }

        public void EnterLobby(string result)
        {
            LogMsg(result);

            Invoke((Action)(() =>
            {
                if (result.ToLower() == "ok")
                {
                    gbLogin.Enabled = false;

                    if (m_RoomName == null || m_RoomName.Length <= 0)
                    {
                        gbLobby.Enabled = true;
                        m_UserList.Clear();
                        UpdateUserList();
                        richChatBox.Clear();
                    }

                    if (m_UserName.Length > 0 && m_UserToken.Length > 0 && m_RoomName.Length > 0)
                    {
                        gbChatRoom.Enabled = true;
                        ShowText("Re-enter lobby/chat-room successfully");
                    }

                    if (m_UserName.Length > 0 && m_UserToken.Length > 0)
                    {
                        GetRoomListRequest request = new GetRoomListRequest();
                        request.UserName = m_UserName;
                        request.UserToken = m_UserToken;

                        m_Client.SendString("lobby/list/" + WebMessage.ToJsonString<GetRoomListRequest>(request));
                    }
                }
                else
                {
                    ShowText("ERROR: " + "Failed to enter lobby: " + result);
                }
            }));

        }

        public void UpdateRoomList(List<RoomSummary> rooms)
        {
            LogMsg("Total Rooms: " + rooms.Count);

            Invoke((Action)(() =>
            {
                listRooms.Items.Clear();
                foreach (RoomSummary room in rooms) listRooms.Items.Add(room.RoomName + " (" + room.UserCount + ")");
            }));

        }

        public void CreateRoom(string roomName, string result)
        {
            LogMsg(result);
            Invoke((Action)(() =>
            {
                if (result.ToLower() != "ok") MessageBox.Show(result);
                else
                {
                    EnterRoom(roomName, "ok");
                }
            }));
        }

        public void EnterRoom(string roomName, string result)
        {
            LogMsg(result);
            Invoke((Action)(() =>
            {
                if (result.ToLower() != "ok") MessageBox.Show(result);
                else
                {
                    m_RoomName = roomName;

                    gbChatRoom.Text = "Room - " + roomName;
                    gbChatRoom.Enabled = true;

                    gbLobby.Enabled = false;

                    if (cbbChatToWho.Items.Count > 0) cbbChatToWho.SelectedIndex = 0;
                    cbbChatToWho.Text = "";
                }
            }));
        }

        public void ExitRoom(string result)
        {
            LogMsg(result);
            Invoke((Action)(() =>
            {
                if (result.ToLower() != "ok") MessageBox.Show(result);
                else
                {
                    m_RoomName = "";

                    gbChatRoom.Text = "Room";
                    gbChatRoom.Enabled = false;

                    gbLobby.Enabled = true;

                    richChatBox.Clear();

                    if (cbbChatToWho.Items.Count > 0) cbbChatToWho.SelectedIndex = 0;
                    cbbChatToWho.Text = "";
                }
            }));

            UpdateUserList(new List<string>());
        }

        public void UpdateUserList(List<String> users)
        {
            Invoke((Action)(() =>
            {
                m_UserList.Clear();
                m_UserList.AddRange(users);

                UpdateUserList();
            }));
        }

        public void ReceiveChatMessage(SendMessageReply reply)
        {
            LogMsg(reply.Words);

            Invoke((Action)(() =>
            {
                if (reply.FromSystem)
                {
                    if (reply.Words.ToLower().Contains("left"))
                    {
                        if (m_UserList.Contains(reply.UserName))
                        {
                            m_UserList.Remove(reply.UserName);

                            UpdateUserList();

                            string target = cbbChatToWho.SelectedIndex > 0 ? cbbChatToWho.Items[cbbChatToWho.SelectedIndex].ToString() : "";
                            if (target == reply.UserName && cbbChatToWho.Items.Count > 0) cbbChatToWho.SelectedIndex = 0;
                        }
                    }
                    else if (reply.Words.ToLower().Contains("entered"))
                    {
                        if (!m_UserList.Contains(reply.UserName))
                        {
                            m_UserList.Add(reply.UserName);

                            UpdateUserList();
                        }
                    }

                    ShowText("SYSTEM: " + reply.Words);
                }
                else
                {
                    string line = reply.UserName;
                    if (reply.Target != null && reply.Target.Length > 0) line += " to " + reply.Target;
                    if (reply.IsPrivate) line += " (in private)";

                    line += " :  " + reply.Words;

                    ShowText(line);
                }
                
            }));
        }

        private void UpdateUserList()
        {
            listUsers.Items.Clear();
            cbbChatToWho.Items.Clear();
            if (m_UserList.Count > 0)
            {
                listUsers.Items.Add("[ALL]");
                cbbChatToWho.Items.Add("[ALL]");
                foreach (String user in m_UserList)
                {
                    listUsers.Items.Add(user);
                    cbbChatToWho.Items.Add(user);
                }
            }
        }

        private void LogMsg(string msg)
        {
            Invoke((Action)(() =>
            {
                Console.WriteLine(msg);
            }));
        }

        private void ShowText(string text)
        {
            if (richChatBox.Lines.Length > 1024)
            {
                List<string> finalLines = richChatBox.Lines.ToList();
                finalLines.RemoveRange(0, 512);
                richChatBox.Lines = finalLines.ToArray();
            }

            richChatBox.AppendText("[" + DateTime.Now.ToString("HH:mm:ss") + "] " + text + Environment.NewLine);
            richChatBox.SelectionStart = richChatBox.Text.Length;
            richChatBox.ScrollToCaret();
        }

        private string Login(string loginUrl, string userName, string password = "", string userToken = "")
        {
            string errMsg = "Invalid input";

            try
            {
                HttpWebRequest httpWebRequest = (HttpWebRequest)HttpWebRequest.Create(loginUrl);
                httpWebRequest.ContentType = "text/plain";
                httpWebRequest.Method = "POST";
                httpWebRequest.KeepAlive = false;

                using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                {
                    LoginRequest request = new LoginRequest();
                    request.UserName = userName;
                    request.Password = password;
                    request.UserToken = userToken;

                    string json = WebMessage.ToJsonString<LoginRequest>(request);

                    streamWriter.Write("login/login/" + json);
                    streamWriter.Flush();

                    //streamWriter.Close();
                }

                string responseJson = "";
                HttpWebResponse response = (HttpWebResponse)httpWebRequest.GetResponse();
                using (StreamReader sr = new StreamReader(response.GetResponseStream()))
                {
                    responseJson = sr.ReadToEnd();

                    LogMsg(responseJson);
                }

                errMsg = "Abnormal reply";
                if (responseJson != null && responseJson.Length > 0)
                {
                    LoginReply reply = WebMessage.ToJsonObject<LoginReply>(responseJson);
                    if (reply != null && reply.Result != null)
                    {
                        if (reply.Result.ToLower() == "ok")
                        {
                            m_UserName = userName;
                            m_UserToken = reply.UserToken;

                            m_Client.Open("ws://" + reply.ServerUri);

                            return "";
                        }

                        errMsg = reply.Result;
                    }
                }
            }
            catch (Exception ex)
            {
                errMsg = ex.Message;
            }

            return errMsg;
        }

        public void OnConnect(Session session)
        {
            LogMsg("Client Connected, ID: " + session.GetId());

            if (m_UserName.Length > 0 && m_UserToken.Length > 0)
            {
                EnterLobbyRequest request = new EnterLobbyRequest();
                request.UserName = m_UserName;
                request.UserToken = m_UserToken;

                m_Client.SendString("lobby/enter/" + WebMessage.ToJsonString<EnterLobbyRequest>(request));
            }

        }

        public void OnDisconnect(Session session)
        {
            LogMsg("Client Disconnected, ID: " + session.GetId());
            Invoke((Action)(() =>
            {
                if (m_UserName.Length > 0 && m_UserToken.Length > 0)
                {
                    ShowText("ERROR: " + "Lost connection to chat server.");
                }
                gbLobby.Enabled = false;
                gbChatRoom.Enabled = false;

                gbLogin.Enabled = false;

                if (m_UserName.Length > 0 && m_UserToken.Length > 0)
                {
                    ShowText("Trying to re-login, please wait...");
                    timerAutoRelogin.Enabled = true;
                    timerAutoRelogin.Start();
                }
                else gbLogin.Enabled = true;

            }));
        }

        public void OnError(Session session, Int32 errortype, String errormsg)
        {
            LogMsg("Client Error (" + session.GetId() + ") : " + errormsg);
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            m_Client = MessageClient.CreateNewClient();

            m_Client.SetClientId(1);
            m_Client.IsOrderlyProcess = true;

            m_Client.Events.OnHandshake += OnConnect;
            m_Client.Events.OnDisconnect += OnDisconnect;
            m_Client.Events.OnError += OnError;

            m_Client.Handlers.AddHandler(new EnterLobbyHandler(this));
            m_Client.Handlers.AddHandler(new GetRoomListHandler(this));
            m_Client.Handlers.AddHandler(new CreateRoomHandler(this));
            m_Client.Handlers.AddHandler(new EnterRoomHandler(this));
            m_Client.Handlers.AddHandler(new ExitRoomHandler(this));
            m_Client.Handlers.AddHandler(new RoomMessageHandler(this));

            listRooms.Items.Clear();
            listUsers.Items.Clear();

            gbChatRoom.Enabled = false;
            gbLobby.Enabled = false;
            gbLogin.Enabled = true;

            timerAutoRelogin.Enabled = false;

            this.AcceptButton = btnSend;

            DisableEditing(cbbChatToWho);
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            timerRefreshRooms.Enabled = false;
            timerAutoRelogin.Enabled = false;

            if (m_Client != null && m_Client.GetState() > 0)
            {
                m_UserName = "";
                m_UserToken = "";
                m_Client.Close();
            }
        }

        private void timerAutoRelogin_Tick(object sender, EventArgs e)
        {
            timerAutoRelogin.Enabled = false;

            if (m_LoginURL.Length > 0 && m_UserName.Length > 0 && m_UserToken.Length > 0)
            {
                string errMsg = Login(m_LoginURL, m_UserName, "", m_UserToken);
                if (errMsg != null && errMsg.Length > 0)
                {
                    gbLogin.Enabled = true;
                    ShowText("ERROR: " + "Failed to auto re-login - " + errMsg);
                }
                else
                {
                    ShowText("Re-login successfully");
                }
            }
            else gbLogin.Enabled = true;
        }

        private void btnLogin_Click(object sender, EventArgs e)
        {
            btnLogin.Enabled = false;

            try
            {
                string requestUrl = edtLoginUrl.Text;
                string userName = edtUserName.Text;

                m_LoginURL = requestUrl;

                m_UserName = "";
                m_UserToken = "";
                m_RoomName = "";

                string errMsg = Login(requestUrl, userName);

                if (errMsg != null && errMsg.Length > 0)
                {
                    gbLogin.Enabled = true;
                    ShowText("ERROR: " + "Failed to login - " + errMsg);
                }
            }
            catch (Exception ex)
            {
                gbLogin.Enabled = true;
                ShowText("ERROR: " + "Failed to login - " + ex.Message);
            }
            finally
            {
                btnLogin.Enabled = true;
            }
        }

        private void btnLogout_Click(object sender, EventArgs e)
        {
            string requestUrl = edtLoginUrl.Text;

            HttpWebRequest httpWebRequest = (HttpWebRequest)HttpWebRequest.Create(requestUrl);
            httpWebRequest.ContentType = "text/plain";
            httpWebRequest.Method = "POST";

            using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
            {
                LogoutRequest request = new LogoutRequest();
                request.UserName = m_UserName;
                request.UserToken = m_UserToken;

                string json = WebMessage.ToJsonString<LogoutRequest>(request);

                streamWriter.Write("login/logout/" + json);
                streamWriter.Flush();

                streamWriter.Close();

            }

            string responseJson = "";
            HttpWebResponse response = (HttpWebResponse)httpWebRequest.GetResponse();
            using (StreamReader sr = new StreamReader(response.GetResponseStream()))
            {
                responseJson = sr.ReadToEnd();

                LogMsg(responseJson);
            }
        }

        private void btnCreateRoom_Click(object sender, EventArgs e)
        {
            string newRoomName = "";

            if (InputBox("Create New Chat Room", "New Room Name", ref newRoomName) == DialogResult.OK)
            {
                if (m_UserName.Length > 0 && m_UserToken.Length > 0)
                {
                    CreateRoomRequest request = new CreateRoomRequest();
                    request.UserName = m_UserName;
                    request.UserToken = m_UserToken;
                    request.RoomName = newRoomName;

                    m_Client.SendString("room/create/" + WebMessage.ToJsonString<CreateRoomRequest>(request));
                }
            }
        }

        private void btnEnterRoom_Click(object sender, EventArgs e)
        {
            if (!gbLobby.Enabled || !btnEnterRoom.Enabled) return;
            if (listRooms.SelectedIndex >= 0 && m_UserName.Length > 0 && m_UserToken.Length > 0)
            {
                string roomName = listRooms.Items[listRooms.SelectedIndex].ToString();

                if (roomName != null && roomName.Length > 0 && roomName.Contains('('))
                    roomName = roomName.Substring(0, roomName.IndexOf('(')).Trim();

                if (roomName != null && roomName.Length > 0)
                {
                    EnterRoomRequest request = new EnterRoomRequest();
                    request.UserName = m_UserName;
                    request.UserToken = m_UserToken;
                    request.RoomName = roomName;

                    m_Client.SendString("room/enter/" + WebMessage.ToJsonString<EnterRoomRequest>(request));
                }
            }
        }

        private void timerRefreshRooms_Tick(object sender, EventArgs e)
        {
            if (m_Client != null && m_Client.GetState() > 0 
                && m_UserName.Length > 0 && m_UserToken.Length > 0)
            {
                GetRoomListRequest request = new GetRoomListRequest();
                request.UserName = m_UserName;
                request.UserToken = m_UserToken;

                m_Client.SendString("lobby/list/" + WebMessage.ToJsonString<GetRoomListRequest>(request));
            }
        }

        private void btnLeave_Click(object sender, EventArgs e)
        {
            if (gbChatRoom.Enabled && btnSend.Enabled
                && m_Client != null && m_Client.GetState() > 0
                && m_UserName.Length > 0 && m_UserToken.Length > 0
                && m_RoomName.Length > 0)
            {
                ExitRoomRequest request = new ExitRoomRequest();
                request.UserName = m_UserName;
                request.UserToken = m_UserToken;
                request.RoomName = m_RoomName;

                m_Client.SendString("room/exit/" + WebMessage.ToJsonString<ExitRoomRequest>(request));
            }
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            if (gbChatRoom.Enabled && btnSend.Enabled
                && m_Client != null && m_Client.GetState() > 0
                && m_UserName.Length > 0 && m_UserToken.Length > 0
                && m_RoomName.Length > 0)
            {
                string words = edtChat.Text;
                if (words != null && words.Length > 0)
                {
                    btnSend.Enabled = false;
                    try
                    {
                        edtChat.Text = "";

                        SendMessageRequest request = new SendMessageRequest();
                        request.UserName = m_UserName;
                        request.UserToken = m_UserToken;
                        request.RoomName = m_RoomName;
                        request.IsPrivate = ckbPrivate.Checked;
                        request.Target = cbbChatToWho.SelectedIndex > 0 ? cbbChatToWho.Items[cbbChatToWho.SelectedIndex].ToString() : "";
                        request.Words = words;

                        m_Client.SendString("room/chat/" + WebMessage.ToJsonString<SendMessageRequest>(request));
                    }
                    finally
                    {
                        btnSend.Enabled = true;
                    }
                }
            }
        }

        private void listRooms_DoubleClick(object sender, EventArgs e)
        {
            btnEnterRoom.PerformClick();
        }

        private void listUsers_DoubleClick(object sender, EventArgs e)
        {
            if (listUsers.SelectedIndex >= 0) cbbChatToWho.SelectedIndex = listUsers.SelectedIndex;
        }

    }
}

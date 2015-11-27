using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using SharpNetwork;
using SharpNetwork.SimpleWebSocket;

namespace SampleChatClient
{
    public class EnterLobbyHandler : MessageHandler<EnterLobbyReply>
    {
        private ChatClient m_Client = null;

        public EnterLobbyHandler(ChatClient client)
            : base()
        {
            m_Client = client;
        }

        public override void ProcessMessage(Session session, EnterLobbyReply msg)
        {
            if (m_Client != null && msg != null) m_Client.EnterLobby(msg.Result);
        }
    }

    public class GetRoomListHandler : MessageHandler<GetRoomListReply>
    {
        private ChatClient m_Client = null;

        public GetRoomListHandler(ChatClient client)
            : base()
        {
            m_Client = client;
        }

        public override void ProcessMessage(Session session, GetRoomListReply msg)
        {
            if (m_Client != null && msg != null) m_Client.UpdateRoomList(msg.Rooms);
        }
    }

    public class CreateRoomHandler : MessageHandler<CreateRoomReply>
    {
        private ChatClient m_Client = null;

        public CreateRoomHandler(ChatClient client)
            : base()
        {
            m_Client = client;
        }

        public override void ProcessMessage(Session session, CreateRoomReply msg)
        {
            if (m_Client != null && msg != null)
            {
                if (msg.Result != null) m_Client.CreateRoom(msg.RoomName, msg.Result);
                if (msg.Result != null && msg.Result.ToLower() == "ok")
                {
                    if (msg.Rooms != null) m_Client.UpdateRoomList(msg.Rooms);
                    if (msg.Users != null) m_Client.UpdateUserList(msg.Users);
                }
            }
        }
    }

    public class EnterRoomHandler : MessageHandler<EnterRoomReply>
    {
        private ChatClient m_Client = null;

        public EnterRoomHandler(ChatClient client)
            : base()
        {
            m_Client = client;
        }

        public override void ProcessMessage(Session session, EnterRoomReply msg)
        {
            if (m_Client != null && msg != null)
            {
                if (msg.Result != null && msg.RoomName != null) m_Client.EnterRoom(msg.RoomName, msg.Result);
                if (msg.Result != null && msg.Result.ToLower() == "ok")
                {
                    if (msg.Users != null) m_Client.UpdateUserList(msg.Users);
                }
            }
        }
    }

    public class ExitRoomHandler : MessageHandler<ExitRoomReply>
    {
        private ChatClient m_Client = null;

        public ExitRoomHandler(ChatClient client)
            : base()
        {
            m_Client = client;
        }

        public override void ProcessMessage(Session session, ExitRoomReply msg)
        {
            if (m_Client != null && msg != null)
            {
                if (msg.Result != null) m_Client.ExitRoom(msg.Result);
                if (msg.Result != null && msg.Result.ToLower() == "ok")
                {
                    if (msg.Rooms != null) m_Client.UpdateRoomList(msg.Rooms);
                }
            }
        }
    }

    public class RoomMessageHandler : MessageHandler<SendMessageReply>
    {
        private ChatClient m_Client = null;

        public RoomMessageHandler(ChatClient client)
            : base()
        {
            m_Client = client;
        }

        public override void ProcessMessage(Session session, SendMessageReply msg)
        {
            if (m_Client != null && msg != null)
            {
                if (msg.Words != null)
                {
                    m_Client.ReceiveChatMessage(msg);
                }
            }
        }
    }
}

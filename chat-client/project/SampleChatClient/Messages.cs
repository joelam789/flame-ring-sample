using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

using SharpNetwork.SimpleWebSocket;

namespace SampleChatClient
{
    [DataContract]
    public class LoginRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        [DataMember(Name = "password")]
        public string Password { get; set; }

        public LoginRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
            Password = "";
        }
    }

    [DataContract]
    public class LoginReply : JsonMessage
    {
        [DataMember(Name = "result")]
        public string Result { get; set; }

        [DataMember(Name = "server_uri")]
        public string ServerUri { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        public LoginReply()
            : base()
        {
            Result = "";
            ServerUri = "";
            UserToken = "";
        }
    }

    [DataContract]
    public class LogoutRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        public LogoutRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
        }
    }

    [DataContract]
    public class EnterLobbyRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        public EnterLobbyRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
        }
    }

    [DataContract]
    public class EnterLobbyReply : JsonMessage
    {
        [DataMember(Name = "result")]
        public string Result { get; set; }

        public EnterLobbyReply()
            : base()
        {
            Result = "";
        }
    }

    [DataContract]
    public class RoomSummary
    {
        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        [DataMember(Name = "creator")]
        public string Creator { get; set; }

        [DataMember(Name = "user_count")]
        public int UserCount { get; set; }

        public RoomSummary()
        {
            RoomName = "";
            Creator = "";
            UserCount = 0;
        }
    }

    [DataContract]
    public class GetRoomListRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        public GetRoomListRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
        }
    }

    [DataContract]
    public class GetRoomListReply : JsonMessage
    {
        [DataMember(Name = "result")]
        public string Result { get; set; }

        [DataMember(Name = "rooms")]
        public List<RoomSummary> Rooms { get; set; }

        public GetRoomListReply()
            : base()
        {
            Result = "";
            Rooms = new List<RoomSummary>();
        }
    }

    [DataContract]
    public class CreateRoomRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        public CreateRoomRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
            RoomName = "";
        }
    }

    [DataContract]
    public class CreateRoomReply : JsonMessage
    {
        [DataMember(Name = "result")]
        public string Result { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        [DataMember(Name = "users")]
        public List<String> Users { get; set; }

        [DataMember(Name = "rooms")]
        public List<RoomSummary> Rooms { get; set; }

        public CreateRoomReply()
            : base()
        {
            Result = "";
            RoomName = "";
            Users = new List<String>();
            Rooms = new List<RoomSummary>();
        }
    }

    [DataContract]
    public class EnterRoomRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        public EnterRoomRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
            RoomName = "";
        }
    }

    [DataContract]
    public class EnterRoomReply : JsonMessage
    {
        [DataMember(Name = "result")]
        public string Result { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        [DataMember(Name = "users")]
        public List<String> Users { get; set; }

        public EnterRoomReply()
            : base()
        {
            Result = "";
            RoomName = "";
            Users = new List<String>();
        }
    }

    [DataContract]
    public class ExitRoomRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        public ExitRoomRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
            RoomName = "";
        }
    }

    [DataContract]
    public class ExitRoomReply : JsonMessage
    {
        [DataMember(Name = "result")]
        public string Result { get; set; }

        [DataMember(Name = "rooms")]
        public List<RoomSummary> Rooms { get; set; }

        public ExitRoomReply()
            : base()
        {
            Result = "";
            Rooms = new List<RoomSummary>();
        }
    }

    [DataContract]
    public class SendMessageRequest : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "user_token")]
        public string UserToken { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        [DataMember(Name = "target")]
        public string Target { get; set; }

        [DataMember(Name = "words")]
        public string Words { get; set; }

        [DataMember(Name = "is_private")]
        public bool IsPrivate { get; set; }

        public SendMessageRequest()
            : base()
        {
            UserName = "";
            UserToken = "";
            RoomName = "";
            Target = "";
            Words = "";
            IsPrivate = false;
        }
    }

    [DataContract]
    public class SendMessageReply : JsonMessage
    {
        [DataMember(Name = "user_name")]
        public string UserName { get; set; }

        [DataMember(Name = "room_name")]
        public string RoomName { get; set; }

        [DataMember(Name = "target")]
        public string Target { get; set; }

        [DataMember(Name = "words")]
        public string Words { get; set; }

        [DataMember(Name = "is_private")]
        public bool IsPrivate { get; set; }

        [DataMember(Name = "from_system")]
        public bool FromSystem { get; set; }

        public SendMessageReply()
            : base()
        {
            UserName = "";
            RoomName = "";
            Target = "";
            Words = "";
            IsPrivate = false;
            FromSystem = false;
        }
    }
}

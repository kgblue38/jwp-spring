package next.model;

public class GuestUser extends User{
	private static GuestUser guestUser = null;
	
	private GuestUser() {
	}
	
	public static GuestUser getInstance() {
		if (guestUser == null) {
			guestUser = new GuestUser();
		}
		return guestUser;
	}

	@Override
	public boolean isGuestUser() {
		return true;
	}
}

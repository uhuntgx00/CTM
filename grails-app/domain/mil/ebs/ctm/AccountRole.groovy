package mil.ebs.ctm

import org.apache.commons.lang.builder.HashCodeBuilder

class AccountRole
        implements Serializable
{
	private static final long serialVersionUID = 1

	Account account
	Role role

	boolean equals(other) {
		if (!(other instanceof AccountRole)) {
			return false
		}

		other.account?.id == account?.id && other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (account) builder.append(account.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static AccountRole get(long accountId, long roleId) {
		where {
            account == load(accountId) && role == load(roleId)
		}.get()
	}

	static AccountRole create(Account account, Role role, boolean flush = false) {
		new AccountRole(account: account, role: role).save(flush: flush, insert: true)
	}

	static boolean remove(Account account, Role role, boolean flush = false) {
		int rowCount = where {
            account == load(account.id) && role == load(role.id)
		}.deleteAll()
		rowCount > 0
	}

	static void removeAll(Account account) {
		where {
            account == load(account.id)
		}.deleteAll()
	}

	static void removeAll(Role role) {
		where {
			role == load(role.id)
		}.deleteAll()
	}

	static mapping = {
		id composite: ['role', 'account']
		version false
	}
}

public class InterestingLocation {
    private String desc;
    private String wiki;
    private String name;
    private Integer rate;
    private String xid;

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Location {\n");
        if (xid != null) sb.append("xid: ").append(xid).append('\n');
        if (name != null) sb.append("name: ").append(name).append('\n');
        if (rate != null)  sb.append("rate: ").append(rate).append('\n');
        if (wiki != null) sb.append("wiki='").append(wiki).append('\n');
        if (desc != null) sb.append("description: ").append(desc).append('\n');
        sb.append('}');
        return sb.toString();
    }
}

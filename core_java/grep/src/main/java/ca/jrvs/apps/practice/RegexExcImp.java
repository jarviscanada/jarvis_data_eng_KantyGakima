package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc {
    @Override
    public boolean matchJpeg(String filename) {
        if(filename == null) return false;
        return filename.matches("(?i)^.+\\.jpe?g$");
    }

    @Override
    public boolean matchIp(String ip) {
        if (ip == null) return false;
        return ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    @Override
    public boolean isEmptyLine(String line) {
        if (line == null) return false;
        return line.matches("^\\s*$");
    }

    //manual test
    public static void main(String [] args ){
        RegexExc regex = new RegexExcImp();
        System.out.println(regex.matchJpeg("photo.JPG"));      // true
        System.out.println(regex.matchJpeg("doc.jpeg "));      // false (extra space)

        System.out.println(regex.matchIp("192.168.1.1"));      // true
        System.out.println(regex.matchIp("999.999.999.999"));  // true
        System.out.println(regex.matchIp("0.0.0.0"));          // true
        System.out.println(regex.matchIp("12.34.56"));         // false

        System.out.println(regex.isEmptyLine(""));             // true
        System.out.println(regex.isEmptyLine("   "));          // true
        System.out.println(regex.isEmptyLine("text"));        // false
    }
}

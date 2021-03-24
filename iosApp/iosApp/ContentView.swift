import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    var body: some View {
        return ZStack(content: {
             RoundedRectangle(cornerRadius: 10.0).fill(Color.white)
             RoundedRectangle(cornerRadius: 10.0).stroke(lineWidth: 3)
             Text(greet()).foregroundColor(Color.orange)
        }).padding()
          .foregroundColor(Color.orange)
          .font(Font.largeTitle)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

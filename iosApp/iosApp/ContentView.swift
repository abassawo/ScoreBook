import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    @State private var gameName: String = ""
    @State private var isEditing = false
    @State private var selection: String? = nil

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                Text("Create a new game").multilineTextAlignment(.center)
                TextField(
                        "Enter a new game",
                         text: $gameName
                    ) { isEditing in
                        self.isEditing = isEditing
                    } onCommit: {
//                         validate(name: gameName)
                    }
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                    .border(Color(UIColor.separator)).padding()
                NavigationLink(destination: Text("Second View"), tag: "Second", selection: $selection) { EmptyView() }
                NavigationLink(destination: Text("Third View"), tag: "Third", selection: $selection) { EmptyView() }


                Button("Start New Game") {
                    self.selection = "Second"
                }
            }
            .navigationBarTitle("Score Book")
            Spacer()
        }.padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
